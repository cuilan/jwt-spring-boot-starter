package com.weattech.boot.jwt.core;

import com.weattech.boot.jwt.config.JwtConfig;
import io.jsonwebtoken.*;
import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JWT 加解密服务
 *
 * @author zhangyan
 * @since 2023/2/14
 */
public class JwtService {

    @Resource
    private JwtConfig jwtConfig;

    /**
     * 创建JWT加密字符串
     *
     * @param userId      用户id
     * @param payloadBean 用户信息载体对象
     * @param <T>         用户信息载体对象
     * @return JWT 加密字符串
     */
    public <T> String createJwt(String userId, T payloadBean) {

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成JWT的时间
        Date iat = new Date();
        long ttlMillIn = iat.getTime() + jwtConfig.getExpiresSecond() * 1000;
        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）

        // 生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(jwtConfig.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> map;
        try {
            map = BeanUtils.describe(payloadBean).entrySet().stream()
                    .filter(n -> n.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JwtBuilder builder = Jwts.builder()
                // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(jwtConfig.getId())
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", signatureAlgorithm.getValue())
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(map)
                // iat: jwt的签发时间
                .setIssuedAt(iat)
                // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roleid之类的，作为什么用户的唯一标志。
                .setSubject(userId)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, signingKey);

        if (ttlMillIn >= 0) {
            builder.setExpiration(new Date(ttlMillIn))
                    .setNotBefore(iat);
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
                .setSigningKey(Base64.getDecoder().decode(base64Security))
                .parseClaimsJws(jsonWebToken).getBody();
    }

    /**
     * 加密并获得用户信息载体对象
     *
     * @param token        jwt加密字符串
     * @param payloadClass 用户信息载体类
     * @param <T>          用户信息载体对象
     * @return 用户信息载体对象
     */
    public <T> T getPayload(String token, Class<T> payloadClass) {
        try {
            Claims claims = parseJWT(token, jwtConfig.getBase64Secret());
            if (claims.getExpiration().before(new Date())) {
                throw new JwtException("JWT 时间过期");
            }
            T t = payloadClass.getDeclaredConstructor().newInstance();
            BeanUtils.populate(t, claims);
            return t;
        } catch (Exception e) {
            throw new JwtException(e.getMessage(), e.getCause());
        }
    }

}
