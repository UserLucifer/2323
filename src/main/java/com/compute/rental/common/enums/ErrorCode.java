package com.compute.rental.common.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    SUCCESS(0, "成功", HttpStatus.OK),
    BAD_REQUEST(400, "请求参数错误", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "未登录或登录已过期", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "无权限访问", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "资源不存在", HttpStatus.NOT_FOUND),
    VALIDATION_FAILED(422, "参数校验失败", HttpStatus.UNPROCESSABLE_ENTITY),
    BUSINESS_ERROR(10000, "业务处理失败", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE(10001, "余额不足", HttpStatus.BAD_REQUEST),
    IDEMPOTENCY_CONFLICT(10002, "重复操作", HttpStatus.CONFLICT),
    TOO_MANY_REQUESTS(10003, "请求过于频繁", HttpStatus.TOO_MANY_REQUESTS),
    WALLET_NOT_FOUND(11001, "钱包不存在", HttpStatus.NOT_FOUND),
    WALLET_DISABLED(11002, "钱包已禁用", HttpStatus.FORBIDDEN),
    INVALID_AMOUNT(11003, "金额无效", HttpStatus.BAD_REQUEST),
    CONCURRENT_UPDATE_FAILED(11004, "数据状态已变化，请刷新后重试", HttpStatus.CONFLICT),
    SYSTEM_ERROR(500, "系统异常，请稍后重试", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
