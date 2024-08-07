package org.skywise.skyworks.controller;

import io.jsonwebtoken.Claims;
import org.skywise.skyworks.common.utils.JwtUtil;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 10:35
 * @Description:
 */
public class one {

    public static void main(String[] args) {
        String jwtStr = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNzIzMzQ2NjU0LCJlbWFpbCI6IjEyNzA3NjI3NTNAcXEuY29tIn0.0scPaI2FJiaV0CESpRzQV2-67fhjqn7xq5Wj4EG5il0";
        String secret = "eyJhbGciOiJIUzI1NiJ9";
        Claims claims = JwtUtil.parseToken(jwtStr, secret);
        System.out.println(claims.get("userId", Integer.class));
        System.out.println(claims.get("email", String.class));

    }
}
