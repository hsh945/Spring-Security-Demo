package vip.erichong.chasingDreams.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author eric
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    private long expire;
    private long refreshExpire;
    private String secret;
    private String header;

    /**
     * 获取签名密钥
     * 与旧版本的区别JJWT（Java JWT）从 0.11.0 版本 开始弃用基于字符串的 signWith 方法
     * 并在 0.11.5 版本 中完全强制执行使用 Key 对象。这一变更的主要目的是 增强安全性 和 明确密钥类型
     */
    public SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 生成 token
     * <br />
     * 以下是旧版的使用方式
     *
     * <p>示例代码：
     * <pre>
     * Jwts.builder().setHeaderParam("typ", "JWT")
     *       .setSubject(username)
     *       .setIssuedAt(nowDate)
     *       .setExpiration(expiryDate)
     *       .signWith(SignatureAlgorithm.HS512, secret) // 这里的 secret 是纯字符串的
     *       .compact()
     * </pre>
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * expire);

        return Jwts.builder()
                // 顺带提一嘴，setHeaderParam 为可选配置
                // 是可省略的配置项，符合 RFC 7519 标准。其中提到 typ = JWT 为默认值，不声明时，依旧会按默认值来解析
                // 如果你想要让生成的 token 更简洁，可以不配置这一项
                // 不配置头部参数就不会记录 token 类型（typ）和签名算法（alg）所以生成的 token 更简洁
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * refreshExpire);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 解析JWT
     * <br />
     *
     * <p>旧方式代码示例</p>
     * <pre>
     * // 这里的 secret 依旧是纯字符串
     * jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
     * </pre>
     */
    public Claims getClaimByToken(String jwt) {
        try {
            // 创建新的 JwtParser 实例
            JwtParser parser = Jwts.parserBuilder()
                    // 使用 SecretKey 对象
                    .setSigningKey(getSigningKey())
                    .build();
            return parser.parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            // 记录日志或处理异常
            return null;
        }
    }

    /**
     * 验证 JWT 是否过期
     */
    public boolean isTokenExpire(Claims claims) {
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
