package com.compute.rental.modules.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.compute.rental.common.enums.BlogPublishStatus;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.modules.blog.dto.BlogPostRequest;
import com.compute.rental.modules.blog.entity.BlogCategory;
import com.compute.rental.modules.blog.entity.BlogPost;
import com.compute.rental.modules.blog.entity.BlogPostTag;
import com.compute.rental.modules.blog.entity.BlogTag;
import com.compute.rental.modules.blog.mapper.BlogCategoryMapper;
import com.compute.rental.modules.blog.mapper.BlogPostMapper;
import com.compute.rental.modules.blog.mapper.BlogPostTagMapper;
import com.compute.rental.modules.blog.mapper.BlogTagMapper;
import com.compute.rental.modules.system.service.AdminLogService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class BlogService {

    private final BlogCategoryMapper categoryMapper;
    private final BlogTagMapper tagMapper;
    private final BlogPostMapper postMapper;
    private final BlogPostTagMapper postTagMapper;
    private final AdminLogService adminLogService;

    public BlogService(
            BlogCategoryMapper categoryMapper,
            BlogTagMapper tagMapper,
            BlogPostMapper postMapper,
            BlogPostTagMapper postTagMapper,
            AdminLogService adminLogService
    ) {
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.postMapper = postMapper;
        this.postTagMapper = postTagMapper;
        this.adminLogService = adminLogService;
    }

    public List<BlogCategory> publicCategories() {
        return categoryMapper.selectList(new LambdaQueryWrapper<BlogCategory>()
                .eq(BlogCategory::getStatus, CommonStatus.ENABLED.value())
                .orderByAsc(BlogCategory::getSortNo)
                .orderByDesc(BlogCategory::getId));
    }

    public List<BlogTag> publicTags() {
        return tagMapper.selectList(new LambdaQueryWrapper<BlogTag>()
                .eq(BlogTag::getStatus, CommonStatus.ENABLED.value())
                .orderByAsc(BlogTag::getSortNo)
                .orderByDesc(BlogTag::getId));
    }

    public PageResult<Map<String, Object>> publicPosts(long pageNo, long pageSize, Long categoryId, Long tagId,
                                                       LocalDateTime startTime, LocalDateTime endTime) {
        return pagePosts(pageNo, pageSize, categoryId, tagId, BlogPublishStatus.PUBLISHED.value(),
                startTime, endTime, true);
    }

    public Map<String, Object> publicPost(Long id) {
        var post = postMapper.selectOne(new LambdaQueryWrapper<BlogPost>()
                .eq(BlogPost::getId, id)
                .eq(BlogPost::getPublishStatus, BlogPublishStatus.PUBLISHED.value())
                .last("LIMIT 1"));
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Blog post not found");
        }
        postMapper.update(null, new LambdaUpdateWrapper<BlogPost>()
                .eq(BlogPost::getId, id)
                .setSql("view_count = view_count + 1"));
        return postMap(post);
    }

    public PageResult<BlogCategory> adminCategories(long pageNo, long pageSize, Integer status) {
        var page = new Page<BlogCategory>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<BlogCategory>()
                .eq(status != null, BlogCategory::getStatus, status)
                .orderByAsc(BlogCategory::getSortNo)
                .orderByDesc(BlogCategory::getId);
        var result = categoryMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional
    public BlogCategory createCategory(BlogCategory category, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        category.setId(null);
        category.setStatus(defaultStatus(category.getStatus()));
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        categoryMapper.insert(category);
        log(adminId, "CREATE_BLOG_CATEGORY", "blog_category", category.getId(), category.getCategoryName(), ip);
        return category;
    }

    @Transactional
    public BlogCategory updateCategory(Long id, BlogCategory request, Long adminId, String ip) {
        requireCategory(id);
        request.setId(id);
        request.setUpdatedAt(DateTimeUtils.now());
        categoryMapper.updateById(request);
        log(adminId, "UPDATE_BLOG_CATEGORY", "blog_category", id, request.getCategoryName(), ip);
        return requireCategory(id);
    }

    @Transactional
    public BlogCategory setCategoryStatus(Long id, Integer status, Long adminId, String ip) {
        requireCategory(id);
        categoryMapper.update(null, new LambdaUpdateWrapper<BlogCategory>()
                .eq(BlogCategory::getId, id)
                .set(BlogCategory::getStatus, status)
                .set(BlogCategory::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, statusAction("BLOG_CATEGORY", status), "blog_category", id, "status=" + status, ip);
        return requireCategory(id);
    }

    public PageResult<BlogTag> adminTags(long pageNo, long pageSize, Integer status) {
        var page = new Page<BlogTag>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<BlogTag>()
                .eq(status != null, BlogTag::getStatus, status)
                .orderByAsc(BlogTag::getSortNo)
                .orderByDesc(BlogTag::getId);
        var result = tagMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional
    public BlogTag createTag(BlogTag tag, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        tag.setId(null);
        tag.setStatus(defaultStatus(tag.getStatus()));
        tag.setCreatedAt(now);
        tag.setUpdatedAt(now);
        tagMapper.insert(tag);
        log(adminId, "CREATE_BLOG_TAG", "blog_tag", tag.getId(), tag.getTagName(), ip);
        return tag;
    }

    @Transactional
    public BlogTag updateTag(Long id, BlogTag request, Long adminId, String ip) {
        requireTag(id);
        request.setId(id);
        request.setUpdatedAt(DateTimeUtils.now());
        tagMapper.updateById(request);
        log(adminId, "UPDATE_BLOG_TAG", "blog_tag", id, request.getTagName(), ip);
        return requireTag(id);
    }

    @Transactional
    public BlogTag setTagStatus(Long id, Integer status, Long adminId, String ip) {
        requireTag(id);
        tagMapper.update(null, new LambdaUpdateWrapper<BlogTag>()
                .eq(BlogTag::getId, id)
                .set(BlogTag::getStatus, status)
                .set(BlogTag::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, statusAction("BLOG_TAG", status), "blog_tag", id, "status=" + status, ip);
        return requireTag(id);
    }

    public PageResult<Map<String, Object>> adminPosts(long pageNo, long pageSize, Long categoryId, Long tagId,
                                                      Integer publishStatus, LocalDateTime startTime,
                                                      LocalDateTime endTime) {
        return pagePosts(pageNo, pageSize, categoryId, tagId, publishStatus, startTime, endTime, false);
    }

    public Map<String, Object> adminPost(Long id) {
        return postMap(requirePost(id));
    }

    @Transactional
    public Map<String, Object> createPost(BlogPostRequest request, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        var post = new BlogPost();
        applyPostRequest(post, request);
        post.setPublishStatus(request.publishStatus() == null ? BlogPublishStatus.DRAFT.value() : request.publishStatus());
        post.setViewCount(0L);
        post.setCreatedBy(adminId);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        if (Integer.valueOf(BlogPublishStatus.PUBLISHED.value()).equals(post.getPublishStatus())) {
            post.setPublishedAt(now);
        }
        postMapper.insert(post);
        replaceTags(post.getId(), request.tagIds());
        log(adminId, "CREATE_BLOG_POST", "blog_post", post.getId(), post.getTitle(), ip);
        return postMap(postMapper.selectById(post.getId()));
    }

    @Transactional
    public Map<String, Object> updatePost(Long id, BlogPostRequest request, Long adminId, String ip) {
        var post = requirePost(id);
        applyPostRequest(post, request);
        post.setUpdatedAt(DateTimeUtils.now());
        postMapper.updateById(post);
        replaceTags(id, request.tagIds());
        log(adminId, "UPDATE_BLOG_POST", "blog_post", id, post.getTitle(), ip);
        return postMap(requirePost(id));
    }

    @Transactional
    public Map<String, Object> publishPost(Long id, Long adminId, String ip) {
        requirePost(id);
        var now = DateTimeUtils.now();
        postMapper.update(null, new LambdaUpdateWrapper<BlogPost>()
                .eq(BlogPost::getId, id)
                .set(BlogPost::getPublishStatus, BlogPublishStatus.PUBLISHED.value())
                .set(BlogPost::getPublishedAt, now)
                .set(BlogPost::getUpdatedAt, now));
        log(adminId, "PUBLISH_BLOG_POST", "blog_post", id, "published", ip);
        return postMap(requirePost(id));
    }

    @Transactional
    public Map<String, Object> unpublishPost(Long id, Long adminId, String ip) {
        requirePost(id);
        postMapper.update(null, new LambdaUpdateWrapper<BlogPost>()
                .eq(BlogPost::getId, id)
                .set(BlogPost::getPublishStatus, BlogPublishStatus.OFFLINE.value())
                .set(BlogPost::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, "UNPUBLISH_BLOG_POST", "blog_post", id, "offline", ip);
        return postMap(requirePost(id));
    }

    @Transactional
    public void deletePost(Long id, Long adminId, String ip) {
        requirePost(id);
        postTagMapper.delete(new LambdaQueryWrapper<BlogPostTag>().eq(BlogPostTag::getPostId, id));
        postMapper.deleteById(id);
        log(adminId, "DELETE_BLOG_POST", "blog_post", id, "deleted", ip);
    }

    private PageResult<Map<String, Object>> pagePosts(long pageNo, long pageSize, Long categoryId, Long tagId,
                                                      Integer publishStatus, LocalDateTime startTime,
                                                      LocalDateTime endTime, boolean publicOnly) {
        var postIds = postIdsByTag(tagId);
        if (tagId != null && postIds.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), 0, pageNo, pageSize);
        }
        var page = new Page<BlogPost>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<BlogPost>()
                .eq(categoryId != null, BlogPost::getCategoryId, categoryId)
                .in(tagId != null, BlogPost::getId, postIds)
                .eq(publicOnly, BlogPost::getPublishStatus, BlogPublishStatus.PUBLISHED.value())
                .eq(!publicOnly && publishStatus != null, BlogPost::getPublishStatus, publishStatus)
                .ge(startTime != null, BlogPost::getPublishedAt, startTime)
                .le(endTime != null, BlogPost::getPublishedAt, endTime)
                .orderByDesc(BlogPost::getIsTop)
                .orderByAsc(BlogPost::getSortNo)
                .orderByDesc(BlogPost::getPublishedAt)
                .orderByDesc(BlogPost::getId);
        var result = postMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords().stream().map(this::postMap).toList(),
                result.getTotal(), result.getCurrent(), result.getSize());
    }

    private List<Long> postIdsByTag(Long tagId) {
        if (tagId == null) {
            return Collections.emptyList();
        }
        return postTagMapper.selectList(new LambdaQueryWrapper<BlogPostTag>()
                        .eq(BlogPostTag::getTagId, tagId))
                .stream()
                .map(BlogPostTag::getPostId)
                .toList();
    }

    private void applyPostRequest(BlogPost post, BlogPostRequest request) {
        post.setCategoryId(request.categoryId());
        post.setTitle(request.title());
        post.setSummary(request.summary());
        post.setCoverImageUrl(request.coverImageUrl());
        post.setContentMarkdown(request.contentMarkdown());
        if (request.publishStatus() != null) {
            post.setPublishStatus(request.publishStatus());
        }
        post.setIsTop(request.isTop() == null ? 0 : request.isTop());
        post.setSortNo(request.sortNo() == null ? 0 : request.sortNo());
    }

    private void replaceTags(Long postId, List<Long> tagIds) {
        postTagMapper.delete(new LambdaQueryWrapper<BlogPostTag>().eq(BlogPostTag::getPostId, postId));
        if (tagIds == null) {
            return;
        }
        var now = DateTimeUtils.now();
        for (var tagId : tagIds.stream().distinct().toList()) {
            var relation = new BlogPostTag();
            relation.setPostId(postId);
            relation.setTagId(tagId);
            relation.setCreatedAt(now);
            postTagMapper.insert(relation);
        }
    }

    private Map<String, Object> postMap(BlogPost post) {
        var result = new LinkedHashMap<String, Object>();
        result.put("id", post.getId());
        result.put("category_id", post.getCategoryId());
        result.put("category_name", categoryName(post.getCategoryId()));
        result.put("title", post.getTitle());
        result.put("summary", post.getSummary());
        result.put("cover_image_url", post.getCoverImageUrl());
        result.put("content_markdown", post.getContentMarkdown());
        result.put("publish_status", post.getPublishStatus());
        result.put("published_at", post.getPublishedAt());
        result.put("is_top", post.getIsTop());
        result.put("sort_no", post.getSortNo());
        result.put("view_count", post.getViewCount());
        result.put("created_by", post.getCreatedBy());
        result.put("tag_ids", tagIds(post.getId()));
        result.put("created_at", post.getCreatedAt());
        result.put("updated_at", post.getUpdatedAt());
        return result;
    }

    private String categoryName(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        var category = categoryMapper.selectById(categoryId);
        return category == null ? null : category.getCategoryName();
    }

    private List<Long> tagIds(Long postId) {
        return postTagMapper.selectList(new LambdaQueryWrapper<BlogPostTag>()
                        .eq(BlogPostTag::getPostId, postId))
                .stream()
                .map(BlogPostTag::getTagId)
                .toList();
    }

    private BlogCategory requireCategory(Long id) {
        var category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Blog category not found");
        }
        return category;
    }

    private BlogTag requireTag(Long id) {
        var tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Blog tag not found");
        }
        return tag;
    }

    private BlogPost requirePost(Long id) {
        var post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Blog post not found");
        }
        return post;
    }

    private Integer defaultStatus(Integer status) {
        return status == null ? CommonStatus.ENABLED.value() : status;
    }

    private String statusAction(String subject, Integer status) {
        return Integer.valueOf(CommonStatus.ENABLED.value()).equals(status) ? "ENABLE_" + subject : "DISABLE_" + subject;
    }

    private void log(Long adminId, String action, String table, Long targetId, String remark, String ip) {
        adminLogService.log(adminId, action, table, targetId, null, null,
                StringUtils.hasText(remark) ? remark : action, ip);
    }
}
