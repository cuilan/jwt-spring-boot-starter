package com.weattech.boot.jwt.core;

import lombok.Data;

/**
 * Jwt 用户信息载体类
 *
 * @author zhangyan
 * @since 2023/2/14
 */
@Data
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

}
