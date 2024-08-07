package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 17:17
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum UpvoteEnum {
    ARTICLE(1, "文章点赞"),
    COMMENT(2, "评论点赞")
    ;

    final Integer code;
    final String msg;
}
