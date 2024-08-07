package org.skywise.skyworks.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.skywise.skyworks.common.enums.ClaimEnum;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 16:10
 * @Description:
 */
public class JwtUtil {
    public static String generateToken(Integer userId, String email, String secret) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ClaimEnum.USER_ID.getColumnName(), userId);
        claims.put(ClaimEnum.EMAIL.getColumnName(), email);
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secret)//签名秘钥
                .setClaims(claims)//定义自定义内容（载荷）
                .setExpiration(LocalDateTimeUtil.LocalDateTimeToDate(LocalDateTime.now().plusMonths(1L)))//设置有效期为1天
                .compact();
        return jwt;
    }


    /**
     * 解析JWT
     */
    public static Claims parseToken(String jwtStr, String secret) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)//指定签名密钥（必须保证和生成令牌时使用相同的签名密钥）
                .parseClaimsJws(jwtStr)
                .getBody();
        return claims;
    }

    public static void main(String[] args) {
        String secret = "eyJhbGciOiJIUzI1NiJ9";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjQ1NzQ1MjQsInVzZXJJZCI6MCwiZW1haWwiOiJhZG1pbiJ9._nJsq69zITcKgctV2Pg8MdWLHjh-Qekk0wyXwvk6W1A";
        Claims claims = parseToken(token, secret);
        System.out.println(claims);
    }
}
