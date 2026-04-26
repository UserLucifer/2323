package com.compute.rental.modules.recharge.controller;

import com.compute.rental.common.api.ApiResponse;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.modules.recharge.dto.AdminApproveRechargeRequest;
import com.compute.rental.modules.recharge.dto.AdminRejectRechargeRequest;
import com.compute.rental.modules.recharge.dto.RechargeOrderQueryRequest;
import com.compute.rental.modules.recharge.dto.RechargeOrderResponse;
import com.compute.rental.modules.recharge.service.RechargeService;
import com.compute.rental.modules.system.service.AdminLogService;
import com.compute.rental.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Admin Recharge")
@RestController
@RequestMapping("/api/admin/recharge")
public class AdminRechargeController {

    private final RechargeService rechargeService;
    private final AdminLogService adminLogService;

    public AdminRechargeController(RechargeService rechargeService, AdminLogService adminLogService) {
        this.rechargeService = rechargeService;
        this.adminLogService = adminLogService;
    }

    @Operation(summary = "Admin recharge orders")
    @GetMapping("/orders")
    public ApiResponse<PageResult<RechargeOrderResponse>> orders(
            @Valid @ModelAttribute RechargeOrderQueryRequest request
    ) {
        return ApiResponse.success(rechargeService.pageAdminOrders(request));
    }

    @Operation(summary = "Admin recharge order detail")
    @GetMapping("/orders/{rechargeNo}")
    public ApiResponse<RechargeOrderResponse> detail(@PathVariable String rechargeNo) {
        return ApiResponse.success(rechargeService.getAdminOrder(rechargeNo));
    }

    @Operation(summary = "Approve recharge order")
    @PostMapping("/orders/{rechargeNo}/approve")
    public ApiResponse<RechargeOrderResponse> approve(
            @PathVariable String rechargeNo,
            @Valid @RequestBody AdminApproveRechargeRequest request,
            HttpServletRequest httpRequest
    ) {
        var admin = CurrentUser.requiredAdmin();
        var response = rechargeService.approve(rechargeNo, admin.id(), request);
        adminLogService.log(admin.id(), "APPROVE_RECHARGE", "recharge_order", null,
                null, rechargeNo, request.reviewRemark(), adminLogService.clientIp(httpRequest));
        return ApiResponse.success(response);
    }

    @Operation(summary = "Reject recharge order")
    @PostMapping("/orders/{rechargeNo}/reject")
    public ApiResponse<RechargeOrderResponse> reject(
            @PathVariable String rechargeNo,
            @Valid @RequestBody AdminRejectRechargeRequest request,
            HttpServletRequest httpRequest
    ) {
        var admin = CurrentUser.requiredAdmin();
        var response = rechargeService.reject(rechargeNo, admin.id(), request);
        adminLogService.log(admin.id(), "REJECT_RECHARGE", "recharge_order", null,
                null, rechargeNo, request.reviewRemark(), adminLogService.clientIp(httpRequest));
        return ApiResponse.success(response);
    }
}
