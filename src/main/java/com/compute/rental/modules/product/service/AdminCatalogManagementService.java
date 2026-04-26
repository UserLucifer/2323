package com.compute.rental.modules.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.modules.product.entity.AiModel;
import com.compute.rental.modules.product.entity.GpuModel;
import com.compute.rental.modules.product.entity.Product;
import com.compute.rental.modules.product.entity.Region;
import com.compute.rental.modules.product.entity.RentalCycleRule;
import com.compute.rental.modules.product.mapper.AiModelMapper;
import com.compute.rental.modules.product.mapper.GpuModelMapper;
import com.compute.rental.modules.product.mapper.ProductMapper;
import com.compute.rental.modules.product.mapper.RegionMapper;
import com.compute.rental.modules.product.mapper.RentalCycleRuleMapper;
import com.compute.rental.modules.system.service.AdminLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AdminCatalogManagementService {

    private final RegionMapper regionMapper;
    private final GpuModelMapper gpuModelMapper;
    private final ProductMapper productMapper;
    private final AiModelMapper aiModelMapper;
    private final RentalCycleRuleMapper rentalCycleRuleMapper;
    private final AdminLogService adminLogService;

    public AdminCatalogManagementService(
            RegionMapper regionMapper,
            GpuModelMapper gpuModelMapper,
            ProductMapper productMapper,
            AiModelMapper aiModelMapper,
            RentalCycleRuleMapper rentalCycleRuleMapper,
            AdminLogService adminLogService
    ) {
        this.regionMapper = regionMapper;
        this.gpuModelMapper = gpuModelMapper;
        this.productMapper = productMapper;
        this.aiModelMapper = aiModelMapper;
        this.rentalCycleRuleMapper = rentalCycleRuleMapper;
        this.adminLogService = adminLogService;
    }

    public PageResult<Region> pageRegions(long pageNo, long pageSize, String regionCode, Integer status) {
        var page = new Page<Region>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<Region>()
                .eq(hasText(regionCode), Region::getRegionCode, regionCode)
                .eq(status != null, Region::getStatus, status)
                .orderByAsc(Region::getSortNo)
                .orderByDesc(Region::getId);
        var result = regionMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional
    public Region createRegion(Region request, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        request.setId(null);
        request.setStatus(defaultStatus(request.getStatus()));
        request.setCreatedAt(now);
        request.setUpdatedAt(now);
        regionMapper.insert(request);
        log(adminId, "CREATE_REGION", "region", request.getId(), request.getRegionCode(), ip);
        return request;
    }

    @Transactional
    public Region updateRegion(Long id, Region request, Long adminId, String ip) {
        requireRegion(id);
        request.setId(id);
        request.setUpdatedAt(DateTimeUtils.now());
        regionMapper.updateById(request);
        log(adminId, "UPDATE_REGION", "region", id, request.getRegionCode(), ip);
        return requireRegion(id);
    }

    @Transactional
    public Region setRegionStatus(Long id, Integer status, Long adminId, String ip) {
        requireRegion(id);
        regionMapper.update(null, new LambdaUpdateWrapper<Region>()
                .eq(Region::getId, id)
                .set(Region::getStatus, status)
                .set(Region::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, statusAction("REGION", status), "region", id, "status=" + status, ip);
        return requireRegion(id);
    }

    public PageResult<GpuModel> pageGpuModels(long pageNo, long pageSize, String modelCode, Integer status) {
        var page = new Page<GpuModel>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<GpuModel>()
                .eq(hasText(modelCode), GpuModel::getModelCode, modelCode)
                .eq(status != null, GpuModel::getStatus, status)
                .orderByAsc(GpuModel::getSortNo)
                .orderByDesc(GpuModel::getId);
        var result = gpuModelMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional
    public GpuModel createGpuModel(GpuModel request, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        request.setId(null);
        request.setStatus(defaultStatus(request.getStatus()));
        request.setCreatedAt(now);
        request.setUpdatedAt(now);
        gpuModelMapper.insert(request);
        log(adminId, "CREATE_GPU_MODEL", "gpu_model", request.getId(), request.getModelCode(), ip);
        return request;
    }

    @Transactional
    public GpuModel updateGpuModel(Long id, GpuModel request, Long adminId, String ip) {
        requireGpuModel(id);
        request.setId(id);
        request.setUpdatedAt(DateTimeUtils.now());
        gpuModelMapper.updateById(request);
        log(adminId, "UPDATE_GPU_MODEL", "gpu_model", id, request.getModelCode(), ip);
        return requireGpuModel(id);
    }

    @Transactional
    public GpuModel setGpuModelStatus(Long id, Integer status, Long adminId, String ip) {
        requireGpuModel(id);
        gpuModelMapper.update(null, new LambdaUpdateWrapper<GpuModel>()
                .eq(GpuModel::getId, id)
                .set(GpuModel::getStatus, status)
                .set(GpuModel::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, statusAction("GPU_MODEL", status), "gpu_model", id, "status=" + status, ip);
        return requireGpuModel(id);
    }

    public PageResult<Product> pageProducts(long pageNo, long pageSize, String productCode,
                                            Long regionId, Long gpuModelId, Integer status) {
        var page = new Page<Product>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<Product>()
                .eq(hasText(productCode), Product::getProductCode, productCode)
                .eq(regionId != null, Product::getRegionId, regionId)
                .eq(gpuModelId != null, Product::getGpuModelId, gpuModelId)
                .eq(status != null, Product::getStatus, status)
                .orderByAsc(Product::getSortNo)
                .orderByDesc(Product::getId);
        var result = productMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Product getProduct(String productCode) {
        return requireProduct(productCode);
    }

    @Transactional
    public Product createProduct(Product request, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        request.setId(null);
        request.setStatus(defaultStatus(request.getStatus()));
        request.setVersionNo(request.getVersionNo() == null ? 0 : request.getVersionNo());
        request.setCreatedAt(now);
        request.setUpdatedAt(now);
        productMapper.insert(request);
        log(adminId, "CREATE_PRODUCT", "product", request.getId(), request.getProductCode(), ip);
        return request;
    }

    @Transactional
    public Product updateProduct(String productCode, Product request, Long adminId, String ip) {
        var existing = requireProduct(productCode);
        request.setId(existing.getId());
        request.setProductCode(productCode);
        request.setUpdatedAt(DateTimeUtils.now());
        productMapper.updateById(request);
        log(adminId, "UPDATE_PRODUCT", "product", existing.getId(), productCode, ip);
        return requireProduct(productCode);
    }

    @Transactional
    public Product setProductStatus(String productCode, Integer status, Long adminId, String ip) {
        var product = requireProduct(productCode);
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
                .eq(Product::getId, product.getId())
                .set(Product::getStatus, status)
                .set(Product::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, statusAction("PRODUCT", status), "product", product.getId(), "status=" + status, ip);
        return requireProduct(productCode);
    }

    public PageResult<AiModel> pageAiModels(long pageNo, long pageSize, String modelCode, Integer status) {
        var page = new Page<AiModel>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<AiModel>()
                .eq(hasText(modelCode), AiModel::getModelCode, modelCode)
                .eq(status != null, AiModel::getStatus, status)
                .orderByAsc(AiModel::getSortNo)
                .orderByDesc(AiModel::getId);
        var result = aiModelMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional
    public AiModel createAiModel(AiModel request, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        request.setId(null);
        request.setStatus(defaultStatus(request.getStatus()));
        request.setCreatedAt(now);
        request.setUpdatedAt(now);
        aiModelMapper.insert(request);
        log(adminId, "CREATE_AI_MODEL", "ai_model", request.getId(), request.getModelCode(), ip);
        return request;
    }

    @Transactional
    public AiModel updateAiModel(String modelCode, AiModel request, Long adminId, String ip) {
        var existing = requireAiModel(modelCode);
        request.setId(existing.getId());
        request.setModelCode(modelCode);
        request.setUpdatedAt(DateTimeUtils.now());
        aiModelMapper.updateById(request);
        log(adminId, "UPDATE_AI_MODEL", "ai_model", existing.getId(), modelCode, ip);
        return requireAiModel(modelCode);
    }

    @Transactional
    public AiModel setAiModelStatus(String modelCode, Integer status, Long adminId, String ip) {
        var model = requireAiModel(modelCode);
        aiModelMapper.update(null, new LambdaUpdateWrapper<AiModel>()
                .eq(AiModel::getId, model.getId())
                .set(AiModel::getStatus, status)
                .set(AiModel::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, statusAction("AI_MODEL", status), "ai_model", model.getId(), "status=" + status, ip);
        return requireAiModel(modelCode);
    }

    public PageResult<RentalCycleRule> pageCycleRules(long pageNo, long pageSize, String cycleCode, Integer status) {
        var page = new Page<RentalCycleRule>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<RentalCycleRule>()
                .eq(hasText(cycleCode), RentalCycleRule::getCycleCode, cycleCode)
                .eq(status != null, RentalCycleRule::getStatus, status)
                .orderByAsc(RentalCycleRule::getSortNo)
                .orderByDesc(RentalCycleRule::getId);
        var result = rentalCycleRuleMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional
    public RentalCycleRule createCycleRule(RentalCycleRule request, Long adminId, String ip) {
        var now = DateTimeUtils.now();
        request.setId(null);
        request.setStatus(defaultStatus(request.getStatus()));
        request.setCreatedAt(now);
        request.setUpdatedAt(now);
        rentalCycleRuleMapper.insert(request);
        log(adminId, "CREATE_RENTAL_CYCLE_RULE", "rental_cycle_rule", request.getId(), request.getCycleCode(), ip);
        return request;
    }

    @Transactional
    public RentalCycleRule updateCycleRule(String cycleCode, RentalCycleRule request, Long adminId, String ip) {
        var existing = requireCycleRule(cycleCode);
        request.setId(existing.getId());
        request.setCycleCode(cycleCode);
        request.setUpdatedAt(DateTimeUtils.now());
        rentalCycleRuleMapper.updateById(request);
        log(adminId, "UPDATE_RENTAL_CYCLE_RULE", "rental_cycle_rule", existing.getId(), cycleCode, ip);
        return requireCycleRule(cycleCode);
    }

    @Transactional
    public RentalCycleRule setCycleRuleStatus(String cycleCode, Integer status, Long adminId, String ip) {
        var rule = requireCycleRule(cycleCode);
        rentalCycleRuleMapper.update(null, new LambdaUpdateWrapper<RentalCycleRule>()
                .eq(RentalCycleRule::getId, rule.getId())
                .set(RentalCycleRule::getStatus, status)
                .set(RentalCycleRule::getUpdatedAt, DateTimeUtils.now()));
        log(adminId, statusAction("RENTAL_CYCLE_RULE", status), "rental_cycle_rule", rule.getId(), "status=" + status, ip);
        return requireCycleRule(cycleCode);
    }

    private Region requireRegion(Long id) {
        var region = regionMapper.selectById(id);
        if (region == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Region not found");
        }
        return region;
    }

    private GpuModel requireGpuModel(Long id) {
        var gpuModel = gpuModelMapper.selectById(id);
        if (gpuModel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "GPU model not found");
        }
        return gpuModel;
    }

    private Product requireProduct(String productCode) {
        var product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductCode, productCode)
                .last("LIMIT 1"));
        if (product == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Product not found");
        }
        return product;
    }

    private AiModel requireAiModel(String modelCode) {
        var model = aiModelMapper.selectOne(new LambdaQueryWrapper<AiModel>()
                .eq(AiModel::getModelCode, modelCode)
                .last("LIMIT 1"));
        if (model == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "AI model not found");
        }
        return model;
    }

    private RentalCycleRule requireCycleRule(String cycleCode) {
        var rule = rentalCycleRuleMapper.selectOne(new LambdaQueryWrapper<RentalCycleRule>()
                .eq(RentalCycleRule::getCycleCode, cycleCode)
                .last("LIMIT 1"));
        if (rule == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Rental cycle rule not found");
        }
        return rule;
    }

    private Integer defaultStatus(Integer status) {
        return status == null ? CommonStatus.ENABLED.value() : status;
    }

    private String statusAction(String subject, Integer status) {
        return Integer.valueOf(CommonStatus.ENABLED.value()).equals(status) ? "ENABLE_" + subject : "DISABLE_" + subject;
    }

    private void log(Long adminId, String action, String table, Long targetId, String remark, String ip) {
        adminLogService.log(adminId, action, table, targetId, null, null, remark, ip);
    }

    private boolean hasText(String value) {
        return StringUtils.hasText(value);
    }
}
