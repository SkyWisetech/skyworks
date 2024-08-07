package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 9:29
 * @Description: 接口调用成功后的返回信息
 */
@Getter
@AllArgsConstructor
public enum ResMsgEnum {
    UPDATE_NICKNAME(1, "修改个人信息成功"),
    UPDATE_FORBID(2, "禁言操作成功"),
    UPDATE_WHITE_BLACK(3, "设置黑白名单成功"),
    ;

    final Integer code;
    final String msg;

    public static String getMsg(Integer code){
        for(ResMsgEnum msgEnum : ResMsgEnum.values()){
            if(msgEnum.code.equals(code)){
                return msgEnum.msg;
            }
        }
        return null;
    }
}
