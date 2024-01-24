package cn.cuilan.boot.jwt.core;

import cn.cuilan.boot.jwt.config.JwtProperties;
import cn.cuilan.boot.jwt.exceptions.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 加解密服务
 *
 * @author zhangyan
 * @since 2023/2/14
 */
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    /**
     * 创建JWT加密字符串
     *
     * @param userId      用户id
     * @param payloadBean 用户信息载体对象
     * @return JWT 加密字符串
     */
    public String createJwt(String userId, JwtPayload payloadBean) {
        return createJwt(userId, payloadBean, jwtProperties.getBase64Secret());
    }

    /**
     * 创建JWT加密字符串
     *
     * @param userId      用户id
     * @param payloadBean 用户信息载体对象
     * @return JWT 加密字符串
     */
    public String createJwt(String userId, JwtPayload payloadBean, String base64Secret) {
        // Issued At 生成JWT的时间
        Date iat = new Date();
        long ttlMillIn = iat.getTime() + jwtProperties.getExpiresSecond() * 1000;

        // 生成签名密钥
        byte[] encodedKeyBytes = Decoders.BASE64.decode(base64Secret);
        SecretKey key = Keys.hmacShaKeyFor(encodedKeyBytes);

        JwtBuilder builder = Jwts.builder()
                .header()
                .and()
                // 设置JWT id
                .id(jwtProperties.getId())
                .claim("userId", payloadBean.getUserId())
                .claim("loginSign", payloadBean.getLoginSign())
                // iat: jwt的签发时间
                .issuedAt(iat)
                .subject(userId)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(key);

        if (ttlMillIn >= 0) {
            builder.expiration(new Date(ttlMillIn))
                    .notBefore(iat);
        }
        // 压缩为jwt
        return builder.compact();
    }

    /**
     * 解密
     *
     * @param jsonWebToken   JWT 加密字符串
     * @param base64Security BASE64安全码
     * @return Claims 对象
     */
    private Claims parseJWT(String jsonWebToken, String base64Security) {
        return Jwts.parser()
                .setSigningKey(Decoders.BASE64.decode(base64Security))
                .build()
                .parseSignedClaims(jsonWebToken)
                .getPayload();
    }

    /**
     * 加密并获得用户信息载体对象
     *
     * @param token jwt加密字符串
     * @return 用户信息载体对象
     */
    public JwtPayload getPayload(String token) {
        return getPayload(token, jwtProperties.getBase64Secret());
    }

    /**
     * 加密并获得用户信息载体对象
     *
     * @param token        jwt加密字符串
     * @param base64Secret 秘钥
     * @return 用户信息载体对象
     */
    public JwtPayload getPayload(String token, String base64Secret) {
        Claims claims = parseJWT(token, base64Secret);
        if (claims.getExpiration().before(new Date())) {
            throw new TokenException("JWT 时间过期");
        }
        JwtPayload payload = new JwtPayload();
        payload.setUserId(Long.parseLong(claims.get("userId").toString()));
        payload.setLoginSign(claims.get("loginSign").toString());
        return payload;
    }

}
