package com.weattech.boot.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jwt配置
 *
 * @author zhangyan
 * @since 2023/2/14
 */
@Component
@ConfigurationProperties(prefix = "weattech.jwt")
public class JwtConfig {

    /**
     * jwt唯一id
     */
    private String id = "weattech-jwt";

    /**
     * 加密
     */
    private String base64Secret;

    /**
     * 过期时间，单位秒
     */
    private Long expiresSecond = 3600L;

    public JwtConfig() {
    }

    public JwtConfig(String id, String base64Secret, Long expiresSecond) {
        this.id = id;
        this.base64Secret = base64Secret;
        this.expiresSecond = expiresSecond;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public Long getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(Long expiresSecond) {
        this.expiresSecond = expiresSecond;
    }
}
