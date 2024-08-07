package org.skywise.skyworks.common.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/8/2 星期五 16:25
 * @Description:
 */
@Data
public class ArticleCommentPageVO extends PageVO{
    List<ArticleCommentVO> articleCommentList;
}
