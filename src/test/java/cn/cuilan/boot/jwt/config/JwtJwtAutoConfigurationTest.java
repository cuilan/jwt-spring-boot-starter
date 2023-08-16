package cn.cuilan.boot.jwt.config;

import cn.cuilan.boot.jwt.core.JwtPayload;
import cn.cuilan.boot.jwt.core.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration
public class JwtJwtAutoConfigurationTest {

    private static final Long USER_ID = 1L;
    private static final String LOGIN_SIGN = "abc";

    @Autowired
    private JwtService jwtService;

    @Test
    public void testCreateJwt() {
        JwtPayload userPayload = new JwtPayload(USER_ID, LOGIN_SIGN);
        String token = jwtService.createJwt(USER_ID.toString(), userPayload);
        System.out.println(token);
        Assumptions.assumeTrue(token != null);
    }

    @Test
    public void testGetPayload() {
        JwtPayload userPayload = new JwtPayload(USER_ID, LOGIN_SIGN);
        String token = jwtService.createJwt(USER_ID.toString(), userPayload);
        JwtPayload payload = jwtService.getPayload(token, JwtPayload.class);
        System.out.println("userId: " + payload.getUserId() + ", loginSign: " + payload.getLoginSign());
        Assertions.assertEquals(USER_ID, payload.getUserId());
        Assertions.assertEquals(LOGIN_SIGN, payload.getLoginSign());
    }

}