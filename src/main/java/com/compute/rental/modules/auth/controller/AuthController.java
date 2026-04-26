package com.compute.rental.modules.auth.controller;

import com.compute.rental.common.api.ApiResponse;
import com.compute.rental.modules.auth.dto.LoginRequest;
import com.compute.rental.modules.auth.dto.LoginResponse;
import com.compute.rental.modules.auth.dto.SendEmailCodeRequest;
import com.compute.rental.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Send login email code")
    @PostMapping("/email-code/send")
    public ApiResponse<Void> sendEmailCode(
            @Valid @RequestBody SendEmailCodeRequest request,
            HttpServletRequest servletRequest
    ) {
        authService.sendEmailCode(request, servletRequest.getRemoteAddr());
        return ApiResponse.success();
    }

    @Operation(summary = "Login or register with email code")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }
}
