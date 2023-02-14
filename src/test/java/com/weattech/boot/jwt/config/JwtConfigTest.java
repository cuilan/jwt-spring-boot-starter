package com.weattech.boot.jwt.config;

import com.weattech.boot.jwt.core.JwtPayload;
import com.weattech.boot.jwt.core.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class JwtConfigTest {

    @Resource
    private JwtService jwtService;

    @Test
    public void testCreateJwt() {
        JwtPayload userPayload = new JwtPayload(1L, "abc");
        String token = jwtService.createJwt("1", userPayload);
        System.out.println(token);
    }

    @Test
    public void testGetPayload() {
        JwtPayload userPayload = new JwtPayload(1L, "abc");
        String token = jwtService.createJwt("1", userPayload);
        JwtPayload payload = jwtService.getPayload(token, JwtPayload.class);
        System.out.println("userId: " + payload.getUserId() + ", loginSign: " + payload.getLoginSign());
    }

}