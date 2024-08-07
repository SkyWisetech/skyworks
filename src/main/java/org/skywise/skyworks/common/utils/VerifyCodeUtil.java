package org.skywise.skyworks.common.utils;

import java.util.Random;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 13:43
 * @Description:
 */
public class VerifyCodeUtil {
    /**
     * 随机数6位生成
     */
    public static String generateVerifyCode () {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i=0; i<6; i++)
        {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
