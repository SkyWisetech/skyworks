package org.skywise.skyworks.common.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 13:30
 * @Description: 用户注册
 */
@Data
public class UserDTO {

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 20, message = "昵称长度最低两位，最长不超过20位")
    private String nickName;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message="邮箱格式不正确")
    private String email;

    /**
     * 邮箱验证码
     * */
    @NotBlank(message = "邮箱验证码不能为空")
    @Pattern(regexp="^[0-9]{6}$",message="邮箱验证码位数不够")
    private String verifyCode;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度最低为6位")
    private String password;

    /**
     * 验证密码
     */
    @NotBlank(message = "验证密码不能为空")
    private String verifyPassword;

}
