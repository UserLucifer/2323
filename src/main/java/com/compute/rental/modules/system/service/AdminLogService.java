package com.compute.rental.modules.system.service;

import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.modules.system.entity.SysAdminLog;
import com.compute.rental.modules.system.mapper.SysAdminLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminLogService {

    public static final String ADMIN_LOGIN_SUCCESS = "ADMIN_LOGIN_SUCCESS";
    public static final String ADMIN_LOGIN_FAIL = "ADMIN_LOGIN_FAIL";
    public static final String ADMIN_LOGOUT = "ADMIN_LOGOUT";
    public static final String UPDATE_SYS_CONFIG = "UPDATE_SYS_CONFIG";
    public static final String RUN_SCHEDULER = "RUN_SCHEDULER";

    private final SysAdminLogMapper sysAdminLogMapper;

    public AdminLogService(SysAdminLogMapper sysAdminLogMapper) {
        this.sysAdminLogMapper = sysAdminLogMapper;
    }

    public void log(Long adminId, String action, String targetTable, Long targetId,
                    String beforeValue, String afterValue, String remark, String ip) {
        if (adminId == null) {
            return;
        }
        var log = new SysAdminLog();
        log.setAdminId(adminId);
        log.setAction(action);
        log.setTargetTable(targetTable);
        log.setTargetId(targetId);
        log.setBeforeValue(beforeValue);
        log.setAfterValue(afterValue);
        log.setRemark(trim(remark, 255));
        log.setIp(trim(ip, 64));
        log.setCreatedAt(DateTimeUtils.now());
        sysAdminLogMapper.insert(log);
    }

    public String clientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        var forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }
        var realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    private String trim(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
