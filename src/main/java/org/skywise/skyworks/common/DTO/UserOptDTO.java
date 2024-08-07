package org.skywise.skyworks.common.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 15:02
 * @Description: 查询用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserOptDTO extends PageDTO {
    private Integer id;

    /**
     * 修改用户熟悉类型：
     * 1 修改用户昵称/简介
     * 2 禁言
     * 3 拉黑/拉白
     */
    private Integer updateType;

    /**
     * 昵称
     */
    @Size(max = 20, min = 1, message = "昵称长度在1-20个字符之间，请勿超出")
    private String nickName;

    private String remark;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码
     */
    private String verifyCode;

    /**
     * 密码
     */
    @Size(min = 6, message = "密码长度最低为6位")
    private String password;

    /**
     * 密码
     */
    @Size(min = 6, message = "密码长度最低为6位")
    private String verifyPassword;

    /**
     * 注册日期起
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 注册日期止
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 禁言天数
     */
    @Positive(message = "禁言天数需要大于0")
    private Integer forbidCommentDay;

    private Integer isBlack;


}
