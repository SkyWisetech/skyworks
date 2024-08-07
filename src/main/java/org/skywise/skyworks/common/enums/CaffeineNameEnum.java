package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 15:58
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum CaffeineNameEnum {
    ARTICLE_NUM("article_num", "文章总量"),
    UPVOTE_NUM("upvote_num", "点赞总量"),
    COMMENT_NUM("comment_num", "评论总量"),
    USER_NUM("user_num", "用户总量"),
    VIEW_NUM("view_num", "浏览总量"),
    DAILY_NEW_USER_NUM("daily_new_user_num", "昨日新增用户总量"),
    MONTHLY_NEW_USER_NUM("monthly_new_user_num", "月新增用户总量"),
    ;

    final String name_en;
    final String name_ch;
}
