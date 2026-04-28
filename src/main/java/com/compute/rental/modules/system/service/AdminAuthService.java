package com.compute.rental.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.modules.system.dto.AdminLoginRequest;
import com.compute.rental.modules.system.dto.AdminLoginResponse;
import com.compute.rental.modules.system.dto.AdminMeResponse;
import com.compute.rental.modules.system.entity.SysAdmin;
import com.compute.rental.modules.system.mapper.SysAdminMapper;
import com.compute.rental.security.IdentityType;
import com.compute.rental.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAuthService {

    private final SysAdminMapper sysAdminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminLogService adminLogService;

    public AdminAuthService(
            SysAdminMapper sysAdminMapper,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AdminLogService adminLogService
    ) {
        this.sysAdminMapper = sysAdminMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminLogService = adminLogService;
    }

    @Transactional
    public AdminLoginResponse login(AdminLoginRequest request, String ip) {
        var userName = request.userName().trim();
        var admin = sysAdminMapper.selectOne(new LambdaQueryWrapper<SysAdmin>()
                .eq(SysAdmin::getUserName, userName)
                .last("LIMIT 1"));
        if (admin == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        if (!Integer.valueOf(CommonStatus.ENABLED.value()).equals(admin.getStatus())) {
            adminLogService.log(admin.getId(), AdminLogService.ADMIN_LOGIN_FAIL, "sys_admin", admin.getId(),
                    null, null, "Admin disabled", ip);
            throw new BusinessException(ErrorCode.FORBIDDEN, "管理员账号已禁用");
        }
        if (!passwordEncoder.matches(request.password(), admin.getPasswordHash())) {
            adminLogService.log(admin.getId(), AdminLogService.ADMIN_LOGIN_FAIL, "sys_admin", admin.getId(),
                    null, null, "Bad credentials", ip);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }

        var now = DateTimeUtils.now();
        sysAdminMapper.update(null, new LambdaUpdateWrapper<SysAdmin>()
                .eq(SysAdmin::getId, admin.getId())
                .set(SysAdmin::getLastLoginAt, now)
                .set(SysAdmin::getUpdatedAt, now));
        admin.setLastLoginAt(now);
        adminLogService.log(admin.getId(), AdminLogService.ADMIN_LOGIN_SUCCESS, "sys_admin", admin.getId(),
                null, null, "Admin login success", ip);
        var token = jwtTokenProvider.createAccessToken(
                admin.getId(),
                admin.getUserName(),
                admin.getRole(),
                IdentityType.ADMIN
        );
        return new AdminLoginResponse(token, toMe(admin));
    }

    public AdminMeResponse me(Long adminId) {
        var admin = requireEnabledAdmin(adminId);
        return toMe(admin);
    }

    public void logout(Long adminId, String ip) {
        adminLogService.log(adminId, AdminLogService.ADMIN_LOGOUT, "sys_admin", adminId,
                null, null, "TODO token blacklist is not implemented", ip);
    }

    private SysAdmin requireEnabledAdmin(Long adminId) {
        var admin = sysAdminMapper.selectById(adminId);
        if (admin == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "管理员不存在");
        }
        if (!Integer.valueOf(CommonStatus.ENABLED.value()).equals(admin.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "管理员账号已禁用");
        }
        return admin;
    }

    private AdminMeResponse toMe(SysAdmin admin) {
        return new AdminMeResponse(
                admin.getId(),
                admin.getUserName(),
                admin.getStatus(),
                admin.getRole()
        );
    }
}
