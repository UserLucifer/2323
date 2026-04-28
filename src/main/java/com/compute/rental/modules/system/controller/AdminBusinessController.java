package com.compute.rental.modules.system.controller;

import com.compute.rental.common.api.ApiResponse;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.modules.commission.entity.CommissionRecord;
import com.compute.rental.modules.order.entity.ApiDeployOrder;
import com.compute.rental.modules.order.entity.RentalOrder;
import com.compute.rental.modules.order.entity.RentalProfitRecord;
import com.compute.rental.modules.order.entity.RentalSettlementOrder;
import com.compute.rental.modules.system.dto.AdminApiCredentialResponse;
import com.compute.rental.modules.system.dto.AdminRentalOrderDetailResponse;
import com.compute.rental.modules.system.dto.AdminUserResponse;
import com.compute.rental.modules.system.dto.AdminUserTeamResponse;
import com.compute.rental.modules.system.dto.AdminWalletResponse;
import com.compute.rental.modules.system.entity.SysAdminLog;
import com.compute.rental.modules.system.service.AdminBusinessQueryService;
import com.compute.rental.modules.system.service.AdminLogService;
import com.compute.rental.modules.user.entity.UserTeamRelation;
import com.compute.rental.modules.wallet.entity.UserWallet;
import com.compute.rental.modules.wallet.entity.WalletTransaction;
import com.compute.rental.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Business")
@RestController
@RequestMapping("/api/admin")
public class AdminBusinessController {

    private final AdminBusinessQueryService adminBusinessQueryService;
    private final AdminLogService adminLogService;

    public AdminBusinessController(
            AdminBusinessQueryService adminBusinessQueryService,
            AdminLogService adminLogService
    ) {
        this.adminBusinessQueryService = adminBusinessQueryService;
        this.adminLogService = adminLogService;
    }

    @Operation(summary = "Admin users")
    @GetMapping("/users")
    public ApiResponse<PageResult<AdminUserResponse>> users(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, name = "user_id") String userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageUsers(pageNo, pageSize, email, userId, status,
                startTime, endTime));
    }

    @Operation(summary = "Admin user detail")
    @GetMapping("/users/{userId}")
    public ApiResponse<AdminUserResponse> user(@PathVariable Long userId) {
        return ApiResponse.success(adminBusinessQueryService.getUser(userId));
    }

    @Operation(summary = "Disable user")
    @PostMapping("/users/{userId}/disable")
    public ApiResponse<AdminUserResponse> disableUser(@PathVariable Long userId, HttpServletRequest request) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(adminBusinessQueryService.disableUser(userId, admin.id(), adminLogService.clientIp(request)));
    }

    @Operation(summary = "Enable user")
    @PostMapping("/users/{userId}/enable")
    public ApiResponse<AdminUserResponse> enableUser(@PathVariable Long userId, HttpServletRequest request) {
        var admin = CurrentUser.requiredAdmin();
        return ApiResponse.success(adminBusinessQueryService.enableUser(userId, admin.id(), adminLogService.clientIp(request)));
    }

    @Operation(summary = "Admin wallets")
    @GetMapping("/wallets")
    public ApiResponse<PageResult<AdminWalletResponse>> wallets(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "wallet_no") String walletNo
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageWallets(pageNo, pageSize, userId, walletNo));
    }

    @Operation(summary = "Admin wallet by user")
    @GetMapping("/wallets/{userId}")
    public ApiResponse<UserWallet> wallet(@PathVariable Long userId) {
        return ApiResponse.success(adminBusinessQueryService.getWalletByUser(userId));
    }

    @Operation(summary = "Admin wallet transactions")
    @GetMapping("/wallet-transactions")
    public ApiResponse<PageResult<WalletTransaction>> walletTransactions(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "wallet_no") String walletNo,
            @RequestParam(required = false, name = "tx_type") String txType,
            @RequestParam(required = false, name = "biz_type") String bizType,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageWalletTransactions(pageNo, pageSize, userId,
                walletNo, txType, bizType, startTime, endTime));
    }

    @Operation(summary = "Admin rental orders")
    @GetMapping("/rental/orders")
    public ApiResponse<PageResult<RentalOrder>> rentalOrders(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "order_no") String orderNo,
            @RequestParam(required = false, name = "order_status") String orderStatus,
            @RequestParam(required = false, name = "profit_status") String profitStatus,
            @RequestParam(required = false, name = "settlement_status") String settlementStatus,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageRentalOrders(pageNo, pageSize, userId, orderNo,
                orderStatus, profitStatus, settlementStatus, startTime, endTime));
    }

    @Operation(summary = "Admin rental order detail")
    @GetMapping("/rental/orders/{orderNo}")
    public ApiResponse<AdminRentalOrderDetailResponse> rentalOrder(@PathVariable String orderNo) {
        return ApiResponse.success(adminBusinessQueryService.getRentalOrder(orderNo));
    }

    @Operation(summary = "Admin API credentials")
    @GetMapping("/api-credentials")
    public ApiResponse<PageResult<AdminApiCredentialResponse>> apiCredentials(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "credential_no") String credentialNo,
            @RequestParam(required = false, name = "token_status") String tokenStatus,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageApiCredentials(pageNo, pageSize, userId,
                credentialNo, tokenStatus, startTime, endTime));
    }

    @Operation(summary = "Admin API credential detail")
    @GetMapping("/api-credentials/{credentialNo}")
    public ApiResponse<AdminApiCredentialResponse> apiCredential(@PathVariable String credentialNo) {
        return ApiResponse.success(adminBusinessQueryService.getApiCredential(credentialNo));
    }

    @Operation(summary = "Admin API deploy orders")
    @GetMapping("/api-deploy-orders")
    public ApiResponse<PageResult<ApiDeployOrder>> apiDeployOrders(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "deploy_no") String deployNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageApiDeployOrders(pageNo, pageSize, userId,
                deployNo, status, startTime, endTime));
    }

    @Operation(summary = "Admin API deploy order detail")
    @GetMapping("/api-deploy-orders/{deployNo}")
    public ApiResponse<ApiDeployOrder> apiDeployOrder(@PathVariable String deployNo) {
        return ApiResponse.success(adminBusinessQueryService.getApiDeployOrder(deployNo));
    }

    @Operation(summary = "Admin profit records")
    @GetMapping("/profit/records")
    public ApiResponse<PageResult<RentalProfitRecord>> profitRecords(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "order_no") String orderNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, name = "profit_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate profitDate,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageProfitRecords(pageNo, pageSize, userId, orderNo,
                status, profitDate, startTime, endTime));
    }

    @Operation(summary = "Admin profit record detail")
    @GetMapping("/profit/records/{profitNo}")
    public ApiResponse<RentalProfitRecord> profitRecord(@PathVariable String profitNo) {
        return ApiResponse.success(adminBusinessQueryService.getProfitRecord(profitNo));
    }

    @Operation(summary = "Admin settlement orders")
    @GetMapping("/settlement/orders")
    public ApiResponse<PageResult<RentalSettlementOrder>> settlementOrders(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "order_no") String orderNo,
            @RequestParam(required = false, name = "settlement_type") String settlementType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageSettlementOrders(pageNo, pageSize, userId,
                orderNo, settlementType, status, startTime, endTime));
    }

    @Operation(summary = "Admin settlement order detail")
    @GetMapping("/settlement/orders/{settlementNo}")
    public ApiResponse<RentalSettlementOrder> settlementOrder(@PathVariable String settlementNo) {
        return ApiResponse.success(adminBusinessQueryService.getSettlementOrder(settlementNo));
    }

    @Operation(summary = "Admin commission records")
    @GetMapping("/commission/records")
    public ApiResponse<PageResult<CommissionRecord>> commissionRecords(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "user_id") Long userId,
            @RequestParam(required = false, name = "order_no") String orderNo,
            @RequestParam(required = false, name = "level_no") Integer levelNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageCommissionRecords(pageNo, pageSize, userId,
                orderNo, levelNo, status, startTime, endTime));
    }

    @Operation(summary = "Admin commission record detail")
    @GetMapping("/commission/records/{commissionNo}")
    public ApiResponse<CommissionRecord> commissionRecord(@PathVariable String commissionNo) {
        return ApiResponse.success(adminBusinessQueryService.getCommissionRecord(commissionNo));
    }

    @Operation(summary = "Admin team relations")
    @GetMapping("/team/relations")
    public ApiResponse<PageResult<UserTeamRelation>> teamRelations(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "ancestor_user_id") Long ancestorUserId,
            @RequestParam(required = false, name = "descendant_user_id") Long descendantUserId,
            @RequestParam(required = false, name = "level_depth") Integer levelDepth
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageTeamRelations(pageNo, pageSize, ancestorUserId,
                descendantUserId, levelDepth));
    }

    @Operation(summary = "Admin user team")
    @GetMapping("/users/{userId}/team")
    public ApiResponse<AdminUserTeamResponse> userTeam(@PathVariable Long userId) {
        return ApiResponse.success(adminBusinessQueryService.userTeam(userId));
    }

    @Operation(summary = "Admin operation logs")
    @GetMapping("/logs")
    public ApiResponse<PageResult<SysAdminLog>> logs(
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false, name = "admin_id") Long adminId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false, name = "biz_type") String bizType,
            @RequestParam(required = false, name = "start_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false, name = "end_time")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return ApiResponse.success(adminBusinessQueryService.pageLogs(pageNo, pageSize, adminId, action,
                bizType, startTime, endTime));
    }

    @Operation(summary = "Admin operation log detail")
    @GetMapping("/logs/{id}")
    public ApiResponse<SysAdminLog> log(@PathVariable Long id) {
        return ApiResponse.success(adminBusinessQueryService.getLog(id));
    }
}
