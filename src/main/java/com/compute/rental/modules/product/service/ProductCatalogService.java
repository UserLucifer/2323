package com.compute.rental.modules.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.modules.product.dto.AiModelResponse;
import com.compute.rental.modules.product.dto.GpuModelResponse;
import com.compute.rental.modules.product.dto.ProductQueryRequest;
import com.compute.rental.modules.product.dto.ProductResponse;
import com.compute.rental.modules.product.dto.RegionResponse;
import com.compute.rental.modules.product.dto.RentalCycleRuleResponse;
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
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductCatalogService {

    private final RegionMapper regionMapper;
    private final GpuModelMapper gpuModelMapper;
    private final ProductMapper productMapper;
    private final AiModelMapper aiModelMapper;
    private final RentalCycleRuleMapper rentalCycleRuleMapper;

    public ProductCatalogService(
            RegionMapper regionMapper,
            GpuModelMapper gpuModelMapper,
            ProductMapper productMapper,
            AiModelMapper aiModelMapper,
            RentalCycleRuleMapper rentalCycleRuleMapper
    ) {
        this.regionMapper = regionMapper;
        this.gpuModelMapper = gpuModelMapper;
        this.productMapper = productMapper;
        this.aiModelMapper = aiModelMapper;
        this.rentalCycleRuleMapper = rentalCycleRuleMapper;
    }

    public List<RegionResponse> listEnabledRegions() {
        return regionMapper.selectList(new LambdaQueryWrapper<Region>()
                        .eq(Region::getStatus, CommonStatus.ENABLED.value())
                        .orderByAsc(Region::getSortNo))
                .stream()
                .map(region -> new RegionResponse(region.getId(), region.getRegionCode(), region.getRegionName()))
                .toList();
    }

    public List<GpuModelResponse> listEnabledGpuModels() {
        return gpuModelMapper.selectList(new LambdaQueryWrapper<GpuModel>()
                        .eq(GpuModel::getStatus, CommonStatus.ENABLED.value())
                        .orderByAsc(GpuModel::getSortNo))
                .stream()
                .map(model -> new GpuModelResponse(model.getId(), model.getModelCode(), model.getModelName()))
                .toList();
    }

    public PageResult<ProductResponse> pageEnabledProducts(ProductQueryRequest request) {
        var page = new Page<Product>(request.current(), request.size());
        var wrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, CommonStatus.ENABLED.value())
                .eq(request.regionId() != null, Product::getRegionId, request.regionId())
                .eq(request.gpuModelId() != null, Product::getGpuModelId, request.gpuModelId())
                .orderByAsc(Product::getSortNo);
        var result = productMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords().stream().map(this::toProductResponse).toList(),
                result.getTotal(), result.getCurrent(), result.getSize());
    }

    public ProductResponse getEnabledProduct(String productCode) {
        var product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductCode, productCode)
                .eq(Product::getStatus, CommonStatus.ENABLED.value())
                .last("LIMIT 1"));
        if (product == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "产品不存在");
        }
        return toProductResponse(product);
    }

    public List<AiModelResponse> listEnabledAiModels() {
        return aiModelMapper.selectList(new LambdaQueryWrapper<AiModel>()
                        .eq(AiModel::getStatus, CommonStatus.ENABLED.value())
                        .orderByAsc(AiModel::getSortNo))
                .stream()
                .map(model -> new AiModelResponse(
                        model.getId(),
                        model.getModelCode(),
                        model.getModelName(),
                        model.getVendorName(),
                        model.getLogoUrl(),
                        model.getMonthlyTokenConsumptionTrillion(),
                        model.getTokenUnitPrice(),
                        model.getDeployTechFee()
                ))
                .toList();
    }

    public List<RentalCycleRuleResponse> listEnabledCycleRules() {
        return rentalCycleRuleMapper.selectList(new LambdaQueryWrapper<RentalCycleRule>()
                        .eq(RentalCycleRule::getStatus, CommonStatus.ENABLED.value())
                        .orderByAsc(RentalCycleRule::getSortNo))
                .stream()
                .map(rule -> new RentalCycleRuleResponse(
                        rule.getId(),
                        rule.getCycleCode(),
                        rule.getCycleName(),
                        rule.getCycleDays(),
                        rule.getYieldMultiplier(),
                        rule.getEarlyPenaltyRate()
                ))
                .toList();
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                product.getMachineCode(),
                product.getMachineAlias(),
                regionName(product.getRegionId()),
                gpuModelName(product.getGpuModelId()),
                product.getGpuMemoryGb(),
                product.getGpuPowerTops(),
                product.getRentPrice(),
                product.getTokenOutputPerMinute(),
                product.getTokenOutputPerDay(),
                product.getRentableUntil(),
                product.getTotalStock(),
                product.getAvailableStock(),
                product.getRentedStock(),
                product.getCpuModel(),
                product.getCpuCores(),
                product.getMemoryGb(),
                product.getSystemDiskGb(),
                product.getDataDiskGb(),
                product.getMaxExpandDiskGb(),
                product.getDriverVersion(),
                product.getCudaVersion(),
                product.getHasCacheOptimization()
        );
    }

    private String regionName(Long regionId) {
        if (regionId == null) {
            return null;
        }
        var region = regionMapper.selectById(regionId);
        return region == null ? null : region.getRegionName();
    }

    private String gpuModelName(Long gpuModelId) {
        if (gpuModelId == null) {
            return null;
        }
        var gpuModel = gpuModelMapper.selectById(gpuModelId);
        return gpuModel == null ? null : gpuModel.getModelName();
    }
}
