package com.weattech.boot.jwt.exceptions;

/**
 * Jwt 异常
 *
 * @author zhangyan
 * @since 2023/2/14
 */
public class JwtException extends RuntimeException {

    public JwtException() {
        super();
    }

    public JwtException(Integer code, String msg) {
        super(msg);
    }

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtException(Throwable cause) {
        super(cause);
    }

    protected JwtException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
