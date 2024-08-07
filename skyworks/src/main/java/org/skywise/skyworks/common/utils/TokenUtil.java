package org.skywise.skyworks.common.utils;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.skywise.skyworks.common.enums.ClaimEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LIGHT
 * @Date: 2024/7/11 星期四 17:27
 * @Description:
 */
@Slf4j
@Component //当前拦截器对象由Spring创建和管理
public class TokenUtil {

    private static String secret;

    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal<>();

    @Value("${token.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    //判断线程map是否为空，为空就添加一个map
    public static Map<String, String> getLocalMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    //把用户信息添加到线程map中
    public static void set(String key, String name) {
        Map<String, String> map = getLocalMap();
        map.put(key, name);
    }

    //获得线程map中的数据
    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return map.get(key);
    }

    //把用户信息添加到线程map中
    public static void setUser(Integer userId, String email) {
        Map<String, String> map = getLocalMap();
        map.put(ClaimEnum.USER_ID.getColumnName(), userId.toString());
        map.put("email", email);
    }

    // 获取登陆用户id
    public static Integer getUserId() {
        Map<String, String> map = getLocalMap();
        if (map.containsKey(ClaimEnum.USER_ID.getColumnName())) {
//            log.info("查询到用户ID:{}", map.get(ClaimEnum.USER_ID.getColumnName()));
            return Integer.valueOf(map.get(ClaimEnum.USER_ID.getColumnName()));
        }
//        log.info("未查询到用户ID");
        return null;
    }

    // 获取登陆用户名
    public static String getEmail() {
        Map<String, String> map = getLocalMap();
        return map.get("email");
    }

    public static void saveUserInfoByRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println("token:" + token);
        if (!StringUtils.isEmpty(token)) {
            Claims claims = JwtUtil.parseToken(token, secret);
            Integer userId = (Integer) claims.get(ClaimEnum.USER_ID.getColumnName());
            String email = (String) claims.get(ClaimEnum.EMAIL.getColumnName());
            TokenUtil.setUser(userId, email);
        }
    }

    public static void saveUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println("token:" + token);

        //5.解析token，如果解析失败，返回错误 结果（未登录）
        try {
            if (!StringUtils.hasLength(token)) {
                throw new Exception("token为空");
            }

            // 保存用户信息
            Claims claims = JwtUtil.parseToken(token, secret);
            Integer userId = (Integer) claims.get(ClaimEnum.USER_ID.getColumnName());
            String email = (String) claims.get(ClaimEnum.EMAIL.getColumnName());
            TokenUtil.setUser(userId, email);

        } catch (Exception e) {
            log.error("Token解析出错", e);
        }
    }


}



