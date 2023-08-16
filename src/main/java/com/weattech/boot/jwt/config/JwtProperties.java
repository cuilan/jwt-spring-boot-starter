package com.weattech.boot.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jwt配置
 *
 * @author zhangyan
 * @since 2023/2/14
 */
@Data
@Component
@ConfigurationProperties(prefix = JwtProperties.PREFIX)
public class JwtProperties {

    /**
     * 配置文件前缀
     */
    public static final String PREFIX = "weattech.jwt";
    public static final String BASE64_SECRET_NAME = "base64Secret";

    /**
     * jwt唯一id
     */
    private String id = "weattech-jwt";

    /**
     * 加密
     */
    private String base64Secret;

    /**
     * 加密签名算法
     */
    private String algo;

    /**
     * 过期时间，单位秒
     */
    private Long expiresSecond = 3600L;

    public JwtProperties() {
    }

    public JwtProperties(String id, String base64Secret, Long expiresSecond) {
        this.id = id;
        this.base64Secret = base64Secret;
        this.expiresSecond = expiresSecond;
    }

    public JwtProperties(String id, String base64Secret, String algo, Long expiresSecond) {
        this.id = id;
        this.base64Secret = base64Secret;
        this.algo = algo;
        this.expiresSecond = expiresSecond;
    }

}
