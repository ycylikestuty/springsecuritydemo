package com.example.springsecuritydemo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ycy
 * 一个完整的JwtToken由三部分组成：头部+负载信息+签名
 * header 存放JwtToken签名的算法 | token的类型：{"alg": "HS512","typ": "JWT"}
 * payload 主要存放用户名、创建时间、生成时间：{"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature 生成算法：HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    /**
     * 过期时间
     */
    private long expire;
    /**
     * 加密算法所需的密钥
     */
    private String secret;
    /**
     * 头部
     * 包含令牌类型type和所使用的的加密算法
     */
    private String header;

    //生成JWT
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        //这里其实就是new一个JwtBuilder，设置jwt的body
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                //jwt的签发时间
                .setIssuedAt(nowDate)
                //过期时间（这里是7天过期
                .setExpiration(expireDate)
                //设置签名使用的签名算法和签名使用的秘钥
                //注意这里是签名的加密算法，不是登录时的密码加密算法
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // 解析JWT
    public Claims getClaimsByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    // 判断JWT是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
