package com.compute.rental.modules.blog.controller;

import com.compute.rental.common.api.ApiResponse;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.modules.blog.dto.BlogPostRequest;
import com.compute.rental.modules.blog.entity.BlogCategory;
import com.compute.rental.modules.blog.entity.BlogTag;
import com.compute.rental.modules.blog.service.BlogService;
import com.compute.rental.modules.system.service.AdminLogService;
import com.compute.rental.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Blog")
@RestController
@RequestMapping("/api/admin/blog")
public class AdminBlogController {

    private final BlogService blogService;
    private final AdminLogService adminLogService;

    public AdminBlogController(BlogService blogService, AdminLogService adminLogService) {
        this.blogService = blogService;
        this.adminLogService = adminLogService;
    }

    @Operation(summary = "Admin blog categories")
    @GetMapping("/categories")
    public ApiResponse<PageResult<BlogCategory>> categories(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) Integer status
    ) {
        return ApiResponse.success(blogService.adminCategories(pageNo, pageSize, status));
    }

    @Operation(summary = "Create blog category")
    @PostMapping("/categories")
    public ApiResponse<BlogCategory> createCategory(@RequestBody BlogCategory request, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.createCategory(request, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Update blog category")
    @PutMapping("/categories/{id}")
    public ApiResponse<BlogCategory> updateCategory(
            @PathVariable Long id,
            @RequestBody BlogCategory request,
            HttpServletRequest httpRequest
    ) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.updateCategory(id, request, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Enable blog category")
    @PostMapping("/categories/{id}/enable")
    public ApiResponse<BlogCategory> enableCategory(@PathVariable Long id, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.setCategoryStatus(id, CommonStatus.ENABLED.value(), admin.id(),
                adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Disable blog category")
    @PostMapping("/categories/{id}/disable")
    public ApiResponse<BlogCategory> disableCategory(@PathVariable Long id, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.setCategoryStatus(id, CommonStatus.DISABLED.value(), admin.id(),
                adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Admin blog tags")
    @GetMapping("/tags")
    public ApiResponse<PageResult<BlogTag>> tags(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) Integer status
    ) {
        return ApiResponse.success(blogService.adminTags(pageNo, pageSize, status));
    }

    @Operation(summary = "Create blog tag")
    @PostMapping("/tags")
    public ApiResponse<BlogTag> createTag(@RequestBody BlogTag request, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.createTag(request, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Update blog tag")
    @PutMapping("/tags/{id}")
    public ApiResponse<BlogTag> updateTag(
            @PathVariable Long id,
            @RequestBody BlogTag request,
            HttpServletRequest httpRequest
    ) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.updateTag(id, request, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Enable blog tag")
    @PostMapping("/tags/{id}/enable")
    public ApiResponse<BlogTag> enableTag(@PathVariable Long id, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.setTagStatus(id, CommonStatus.ENABLED.value(), admin.id(),
                adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Disable blog tag")
    @PostMapping("/tags/{id}/disable")
    public ApiResponse<BlogTag> disableTag(@PathVariable Long id, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.setTagStatus(id, CommonStatus.DISABLED.value(), admin.id(),
                adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Admin blog posts")
    @GetMapping("/posts")
    public ApiResponse<PageResult<Map<String, Object>>> posts(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "category_id") Long categoryId,
            @RequestParam(required = false, name = "tag_id") Long tagId,
            @RequestParam(required = false, name = "publish_status") Integer publishStatus,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(blogService.adminPosts(pageNo, pageSize, categoryId, tagId, publishStatus,
                startTime, endTime));
    }

    @Operation(summary = "Admin blog post detail")
    @GetMapping("/posts/{id}")
    public ApiResponse<Map<String, Object>> post(@PathVariable Long id) {
        return ApiResponse.success(blogService.adminPost(id));
    }

    @Operation(summary = "Create blog post")
    @PostMapping("/posts")
    public ApiResponse<Map<String, Object>> createPost(
            @RequestBody BlogPostRequest request,
            HttpServletRequest httpRequest
    ) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.createPost(request, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Update blog post")
    @PutMapping("/posts/{id}")
    public ApiResponse<Map<String, Object>> updatePost(
            @PathVariable Long id,
            @RequestBody BlogPostRequest request,
            HttpServletRequest httpRequest
    ) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.updatePost(id, request, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Publish blog post")
    @PostMapping("/posts/{id}/publish")
    public ApiResponse<Map<String, Object>> publishPost(@PathVariable Long id, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.publishPost(id, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Unpublish blog post")
    @PostMapping("/posts/{id}/unpublish")
    public ApiResponse<Map<String, Object>> unpublishPost(@PathVariable Long id, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(blogService.unpublishPost(id, admin.id(), adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Delete blog post")
    @PostMapping("/posts/{id}/delete")
    public ApiResponse<Void> deletePost(@PathVariable Long id, HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        blogService.deletePost(id, admin.id(), adminLogService.clientIp(httpRequest));
        return ApiResponse.success(null);
    }
}
