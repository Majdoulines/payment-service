package com.marketplace.payment_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Erreur metier : contient le code HTTP et le code fonctionnel de la SFD
 * (ex. 409 ORDER_ALREADY_PAID).
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public BusinessException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static BusinessException badRequest(String code, String message) {
        return new BusinessException(HttpStatus.BAD_REQUEST, code, message);
    }

    public static BusinessException forbidden(String code, String message) {
        return new BusinessException(HttpStatus.FORBIDDEN, code, message);
    }

    public static BusinessException notFound(String code, String message) {
        return new BusinessException(HttpStatus.NOT_FOUND, code, message);
    }

    public static BusinessException conflict(String code, String message) {
        return new BusinessException(HttpStatus.CONFLICT, code, message);
    }

    public static BusinessException unprocessable(String code, String message) {
        return new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY, code, message);
    }

    public static BusinessException unavailable(String code, String message) {
        return new BusinessException(HttpStatus.SERVICE_UNAVAILABLE, code, message);
    }
}