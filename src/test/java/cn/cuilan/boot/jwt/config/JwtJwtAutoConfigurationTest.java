package cn.cuilan.boot.jwt.config;

import cn.cuilan.boot.jwt.core.JwtPayload;
import cn.cuilan.boot.jwt.core.JwtService;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("测试-生成token")
    public void testCreateJwt() {
        JwtPayload userPayload = new JwtPayload(USER_ID, LOGIN_SIGN);
        String token = jwtService.createJwt(USER_ID.toString(), userPayload);
        System.out.println(token);
        Assumptions.assumeTrue(token != null);
    }

    @Test
    @DisplayName("测试-解密token")
    public void testGetPayload() {
        JwtPayload userPayload = new JwtPayload(USER_ID, LOGIN_SIGN);
        String token = jwtService.createJwt(USER_ID.toString(), userPayload);
        System.out.println(token);
        JwtPayload payload = jwtService.getPayload(token);
        System.out.println("userId: " + payload.getUserId() + ", loginSign: " + payload.getLoginSign());
        Assertions.assertEquals(USER_ID, payload.getUserId());
        Assertions.assertEquals(LOGIN_SIGN, payload.getLoginSign());
    }

    @Test
    @DisplayName("测试-不同的秘钥加密解密")
    public void test2Secret() {
        String secret1 = "xlj1MuBcGoTFWveOw76EtqNaYnkRD5VzPbhygJ9C3IUfLiAmSZX08sdpKQ4r2HFj";
        String secret2 = "DkKSVwtls8N1XpI0YFav3ifdrEyh7CTLPxHobWOjUgJzGmucMZQAR2qe9n4B65kM";
        JwtPayload userPayload = new JwtPayload(USER_ID, LOGIN_SIGN);
        String token = jwtService.createJwt(USER_ID.toString(), userPayload, secret1);
        System.out.println(token);

        try {
            jwtService.getPayload(token, secret2);
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
        }

    }

}