package com.compute.rental.modules.system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.compute.rental.modules.system.dto.UpdateSysConfigRequest;
import com.compute.rental.modules.system.entity.SysConfig;
import com.compute.rental.modules.system.mapper.SysConfigMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SysConfigAdminServiceTest {

    @Mock
    private SysConfigMapper sysConfigMapper;

    @Mock
    private AdminLogService adminLogService;

    @BeforeAll
    static void initMybatisPlusTableInfo() {
        var configuration = new Configuration();
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(configuration, ""), SysConfig.class);
    }

    @Test
    void updateAdminConfigShouldWriteAdminLog() {
        var service = new SysConfigService(sysConfigMapper, adminLogService);
        var before = config("withdraw.min_amount", "10", "old");
        var after = config("withdraw.min_amount", "20", "new");
        when(sysConfigMapper.selectOne(any(Wrapper.class))).thenReturn(before);
        when(sysConfigMapper.update(any(), any(Wrapper.class))).thenReturn(1);
        when(sysConfigMapper.selectById(1L)).thenReturn(after);

        var response = service.updateAdminConfig("withdraw.min_amount",
                new UpdateSysConfigRequest("20", "new"), 9L, "127.0.0.1");

        assertThat(response.configValue()).isEqualTo("20");
        verify(adminLogService).log(eq(9L), eq(AdminLogService.UPDATE_SYS_CONFIG), eq("sys_config"),
                eq(1L), any(), any(), eq("Update config withdraw.min_amount"), eq("127.0.0.1"));
    }

    private SysConfig config(String key, String value, String desc) {
        var config = new SysConfig();
        config.setId(1L);
        config.setConfigKey(key);
        config.setConfigValue(value);
        config.setConfigDesc(desc);
        return config;
    }
}
