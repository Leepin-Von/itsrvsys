package com.plotech.itsrvsys.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类，用于生成和解析JWT令牌。
 */
@Component
public class JwtUtil {

    /**
     * JWT令牌的有效期。
     */
    @Value("${com.plotech.token.expire}")
    public long expire;

    /**
     * JWT令牌的签名密钥。
     */
    @Value("${com.plotech.token.key}")
    public String signKey;

    /**
     * 生成JWT令牌。
     *
     * @param claims JWT令牌的声明，包含用户信息等
     * @return 生成的JWT令牌
     */
    public String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                // 添加声明
                .addClaims(claims)
                // 设置签名算法和密钥
                .signWith(SignatureAlgorithm.HS256, signKey)
                // 设置过期时间
                .setExpiration(new Date((System.currentTimeMillis() + expire)))
                // 构建JWT令牌
                .compact();
    }

    /**
     * 解析JWT令牌。
     *
     * @param jwt JWT令牌
     * @return 解析后的声明
     */
    public Claims parseJwt(String jwt) {
        return Jwts.parser()
                // 设置签名密钥
                .setSigningKey(signKey)
                // 解析JWT令牌
                .parseClaimsJws(jwt)
                // 获取声明
                .getBody();
    }
}