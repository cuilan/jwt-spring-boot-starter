package com.weattech.boot.jwt.config;

import com.weattech.boot.jwt.core.JwtService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Properties 属性配置类
 *
 * @author zhangyan
 * @since 2023/2/14
 */
@Configuration
@EnableConfigurationProperties(JwtConfig.class)
public class Config {

    @Bean
    @ConditionalOnProperty(prefix = "jwt", name = "base64Secret")
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    @ConditionalOnProperty(prefix = "weattech.jwt", name = "base64Secret")
    public JwtService jwtService() {
        return new JwtService();
    }

}
