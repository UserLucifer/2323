package com.compute.rental.modules.system.controller;

import com.compute.rental.common.api.ApiResponse;
import com.compute.rental.modules.system.dto.AdminLoginRequest;
import com.compute.rental.modules.system.dto.AdminLoginResponse;
import com.compute.rental.modules.system.dto.AdminMeResponse;
import com.compute.rental.modules.system.service.AdminAuthService;
import com.compute.rental.modules.system.service.AdminLogService;
import com.compute.rental.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Admin Auth")
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final AdminLogService adminLogService;

    public AdminAuthController(AdminAuthService adminAuthService, AdminLogService adminLogService) {
        this.adminAuthService = adminAuthService;
        this.adminLogService = adminLogService;
    }

    @Operation(summary = "Admin login")
    @PostMapping("/login")
    public ApiResponse<AdminLoginResponse> login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.success(adminAuthService.login(request, adminLogService.clientIp(httpRequest)));
    }

    @Operation(summary = "Current admin")
    @GetMapping("/me")
    public ApiResponse<AdminMeResponse> me() {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(adminAuthService.me(admin.id()));
    }

    @Operation(summary = "Admin logout")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest httpRequest) {
        var admin = CurrentUser.requiredAdmin();
        adminAuthService.logout(admin.id(), adminLogService.clientIp(httpRequest));
        return ApiResponse.success();
    }
}
