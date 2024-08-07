package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 10:36
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ReturnCodeEnum {
    SUCCESS(200, "成功"),
    ERROR(300, "字段校验失败"),
    SERVICE_ERROR(301, "业务异常"),
    UNKNOWN_ERROR(302, "拦截未知的运行时异常"),
    SYSTEM_ERROR(303, "系统异常"),
    SEND_VERIFY_CODE_ERROR(304, "验证码发送失败"),
    VERIFY_CODE_ERROR(305, "验证码验证失败"),
    VERIFY_PASSWORD_ERROR(306, "重复密码输入错误，请重新输入"),
    BLACK_ERROR(400, "您已被禁止登录，请联系客服处理"),
    LOGIN_ERROR(307, "用户还未注册，请先注册"),
    TOKEN_ERROR(308, "token验证失败，请重新登陆"),
    REGISTER_ERROR(308, "已经注册过，如忘记密码，可请重置密码"),

    IMAGE_ERROR(309, "图片错误"),
    IMAGE_URL_ERROR(309, "图片路径错误"),
    IMAGE_NAME_ERROR(309, "图片名称不能为空"),
    UPLOAD_IMAGE_ERROR(310, "图片上传失败"),
    EMAIL_ERROR(311, "图片上传失败"),
    ;

    final Integer code;
    final String msg;
}
