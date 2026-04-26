package com.compute.rental.common.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    SUCCESS(0, "success", HttpStatus.OK),
    BAD_REQUEST(400, "Bad request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND),
    VALIDATION_FAILED(422, "Validation failed", HttpStatus.UNPROCESSABLE_ENTITY),
    BUSINESS_ERROR(10000, "Business error", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE(10001, "Insufficient balance", HttpStatus.BAD_REQUEST),
    IDEMPOTENCY_CONFLICT(10002, "Duplicate operation", HttpStatus.CONFLICT),
    TOO_MANY_REQUESTS(10003, "Too many requests", HttpStatus.TOO_MANY_REQUESTS),
    WALLET_NOT_FOUND(11001, "Wallet not found", HttpStatus.NOT_FOUND),
    WALLET_DISABLED(11002, "Wallet disabled", HttpStatus.FORBIDDEN),
    INVALID_AMOUNT(11003, "Invalid amount", HttpStatus.BAD_REQUEST),
    CONCURRENT_UPDATE_FAILED(11004, "Concurrent update failed", HttpStatus.CONFLICT),
    SYSTEM_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

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
