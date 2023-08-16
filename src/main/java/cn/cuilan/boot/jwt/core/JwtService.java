package cn.cuilan.boot.jwt.core;

import cn.cuilan.boot.jwt.config.JwtProperties;
import cn.cuilan.boot.jwt.exceptions.TokenException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;

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
@RequiredArgsConstructor
public class JwtService {

    // Header Algorithm key
    private static final String ALG_KEY = "alg";

    private final JwtProperties jwtProperties;

    public <T> String createJwt(String userId, T payloadBean) {
        String algo = jwtProperties.getAlgo();
        if (algo == null || algo.isEmpty()) {
            return createJwt(userId, payloadBean, SignatureAlgorithm.HS256);
        }
        algo = algo.toUpperCase();
        switch (algo) {
            case "NONE":
                return createJwt(userId, payloadBean, SignatureAlgorithm.NONE);
            case "HS384":
                return createJwt(userId, payloadBean, SignatureAlgorithm.HS384);
            case "HS512":
                return createJwt(userId, payloadBean, SignatureAlgorithm.HS512);
            case "RS256":
                return createJwt(userId, payloadBean, SignatureAlgorithm.RS256);
            case "RS384":
                return createJwt(userId, payloadBean, SignatureAlgorithm.RS384);
            case "RS512":
                return createJwt(userId, payloadBean, SignatureAlgorithm.RS512);
            case "ES256":
                return createJwt(userId, payloadBean, SignatureAlgorithm.ES256);
            case "ES384":
                return createJwt(userId, payloadBean, SignatureAlgorithm.ES384);
            case "ES512":
                return createJwt(userId, payloadBean, SignatureAlgorithm.ES512);
            case "PS256":
                return createJwt(userId, payloadBean, SignatureAlgorithm.PS256);
            case "PS384":
                return createJwt(userId, payloadBean, SignatureAlgorithm.PS384);
            case "PS512":
                return createJwt(userId, payloadBean, SignatureAlgorithm.PS512);
            case "HS256":
            default:
                return createJwt(userId, payloadBean, SignatureAlgorithm.HS256);
        }
    }

    /**
     * 创建JWT加密字符串
     *
     * @param userId             用户id
     * @param payloadBean        用户信息载体对象
     * @param <T>                用户信息载体对象
     * @param signatureAlgorithm 签名算法
     * @return JWT 加密字符串
     */
    public <T> String createJwt(String userId, T payloadBean, SignatureAlgorithm signatureAlgorithm) {
        // Issued At 生成JWT的时间
        Date iat = new Date();
        long ttlMillIn = iat.getTime() + jwtProperties.getExpiresSecond() * 1000;
        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）

        // 生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(jwtProperties.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> map;
        try {
            map = BeanUtils.describe(payloadBean).entrySet().stream()
                    .filter(n -> n.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception e) {
            throw new TokenException(e.getMessage());
        }
        JwtBuilder builder = Jwts.builder()
                // 设置类型为JWT
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // 设置签名算法
                .setHeaderParam(ALG_KEY, signatureAlgorithm.getValue())
                // 设置JWT id
                .setId(jwtProperties.getId())
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
            Claims claims = parseJWT(token, jwtProperties.getBase64Secret());
            if (claims.getExpiration().before(new Date())) {
                throw new TokenException("JWT 时间过期");
            }
            T t = payloadClass.getDeclaredConstructor().newInstance();
            BeanUtils.populate(t, claims);
            return t;
        } catch (Exception e) {
            throw new TokenException(e.getMessage(), e.getCause());
        }
    }

}
