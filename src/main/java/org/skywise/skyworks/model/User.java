package org.skywise.skyworks.model;

import lombok.Data;

@Data
public class User {
    private Integer id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 禁止评论日期，该日期前禁止评论
     */
    private String forbidCommentDate;

    /**
     * 是否进入黑名单，黑名单禁止登录：0否 1是
     */
    private Integer isBlack;

    /**
     * 是否被删除：0否 1是
     */
    private Integer isDelete;

    /**
     * 个人简介
     * */
    private String remark;

    /**
     * 注册日期
     */
    private String createTime;

    /**
     * 更新日期
     */
    private String updateTime;

    public static final String EMAIL_COLUMN = "email";
    public static final String NICKNAME_COLUMN = "nick_name";
    public static final String CREATE_TIME_COLUMN = "create_time";
    public static final String FORBID_COMMENT_COLUMN = "forbid_comment";
    public static final String PASSWORD_COLUMN = "password";


}