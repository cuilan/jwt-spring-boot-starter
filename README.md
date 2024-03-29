# jwt-spring-boot-starter

* Json Web Token `spring-boot-starter`
* 提供 JWT 加解密服务
* SpringBoot2.7.x、SpringBoot3.1.x 已适配

## maven

在 `pom.xml` 文件中加入如下依赖：

```xml
<!-- SpringBoot 2.7.x -->
<dependency>
    <groupId>cn.cuilan.boot</groupId>
    <artifactId>jwt-spring-boot-starter</artifactId>
    <version>spring-2.7.x</version>
</dependency>

<!-- SpringBoot 3.1.x -->
<dependency>
<groupId>cn.cuilan.boot</groupId>
<artifactId>jwt-spring-boot-starter</artifactId>
<version>spring-3.1.x</version>
</dependency>
```

## gradle

```groovy
// SpringBoot 2.7.x
implementation("cn.cuilan.boot:jwt-spring-boot-starter:spring-2.7.x")

// SpringBoot 3.1.x
implementation("cn.cuilan.boot:jwt-spring-boot-starter:spring-3.1.x")
```

## 配置

在 `application.yaml` 或 `application-{env}.yaml` 文件中加入如下配置：

```yaml
jwt:
  # 可选，默认配置：custom-jwt
  id: jwt-test
  # 长度64
  base64Secret: PnJ7gvAwryqKlUQeokI19aXN3fpz6c2tPnJ7gvAwryqKlUQeokI19aXN3fpz6c2t
  # 可选，默认 HS256，可选：HS256、HS384、HS512
  mac: HS256
  # 可选，默认配置：3600，单位：秒
  expiresSecond: 3600
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
        JwtPayload payload = jwtService.getPayload("token_string");
        System.out.println("userId: " + payload.getUserId() + ", loginSign: " + payload.getLoginSign());
    }

}
```
