package com.compute.rental.modules.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.modules.product.entity.Product;
import com.compute.rental.modules.product.mapper.AiModelMapper;
import com.compute.rental.modules.product.mapper.GpuModelMapper;
import com.compute.rental.modules.product.mapper.ProductMapper;
import com.compute.rental.modules.product.mapper.RegionMapper;
import com.compute.rental.modules.product.mapper.RentalCycleRuleMapper;
import com.compute.rental.modules.system.service.AdminLogService;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminCatalogManagementServiceTest {

    @Mock
    private RegionMapper regionMapper;
    @Mock
    private GpuModelMapper gpuModelMapper;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private AiModelMapper aiModelMapper;
    @Mock
    private RentalCycleRuleMapper rentalCycleRuleMapper;
    @Mock
    private AdminLogService adminLogService;

    private AdminCatalogManagementService service;

    @BeforeAll
    static void initMybatisPlusTableInfo() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new Configuration(), ""), Product.class);
    }

    @BeforeEach
    void setUp() {
        service = new AdminCatalogManagementService(regionMapper, gpuModelMapper, productMapper, aiModelMapper,
                rentalCycleRuleMapper, adminLogService);
    }

    @Test
    void disablingProductWritesAdminLog() {
        var product = new Product();
        product.setId(100L);
        product.setProductCode("P100");
        product.setStatus(CommonStatus.ENABLED.value());
        when(productMapper.selectOne(any())).thenReturn(product);
        when(productMapper.update(isNull(), any())).thenReturn(1);

        var result = service.setProductStatus("P100", CommonStatus.DISABLED.value(), 9L, "127.0.0.1");

        assertEquals("P100", result.getProductCode());
        verify(adminLogService).log(eq(9L), eq("DISABLE_PRODUCT"), eq("product"), eq(100L),
                isNull(), isNull(), eq("status=0"), eq("127.0.0.1"));
    }
}
