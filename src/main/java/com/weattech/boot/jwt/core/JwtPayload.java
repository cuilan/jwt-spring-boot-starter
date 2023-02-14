package com.weattech.boot.jwt.core;

/**
 * Jwt 用户信息载体类
 *
 * @author zhangyan
 * @since 2023/2/14
 */
public class JwtPayload {

    private Long userId;

    private String loginSign;

    public JwtPayload() {
    }

    public JwtPayload(Long userId) {
        this.userId = userId;
    }

    public JwtPayload(Long userId, String loginSign) {
        this.userId = userId;
        this.loginSign = loginSign;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginSign() {
        return loginSign;
    }

    public void setLoginSign(String loginSign) {
        this.loginSign = loginSign;
    }
}
