package com.weattech.boot.jwt.exceptions;

/**
 * Jwt 异常
 *
 * @author zhangyan
 * @since 2023/2/14
 */
public class TokenException extends RuntimeException {

    public TokenException() {
        super();
    }

    public TokenException(Integer code, String msg) {
        super(msg);
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    protected TokenException(String message, Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
