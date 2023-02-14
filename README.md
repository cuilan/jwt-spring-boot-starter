# jwt-spring-boot-starter

* Json Web Token `spring-boot-starter`
* 基于 SpringBoot **2.7.6**
* 提供 JWT 加解密服务

## 引入

在 `pom.xml` 文件中加入如下依赖：

```xml

<dependency>
    <groupId>com.weattech.boot</groupId>
    <artifactId>jwt-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 配置

在 `application.yaml` 或 `application-{env}.yaml` 文件中加入如下配置：

```yaml
weattech:
  jwt:
    # 可选，默认配置：weattech-jwt
    id: jwt-test
    base64Secret: PnJ7gvAwryqKlUQeokI19aXN3fpz6c2t
    # 可选，默认配置：3600，单位：秒
    expiresSecond: 3400
```

## 使用

```java
public class JwtConfigTest {

    @Resource
    private JwtService jwtService;

    // 加密
    public void testCreateJwt() {
        JwtPayload userPayload = new JwtPayload(1L, "abc");
        String token = jwtService.createJwt("1", userPayload);
        System.out.println(token);
    }

    // 解密
    public void testGetPayload() {
        JwtPayload payload = jwtService.getPayload("token_string", JwtPayload.class);
        System.out.println("userId: " + payload.getUserId() + ", loginSign: " + payload.getLoginSign());
    }

}
```
