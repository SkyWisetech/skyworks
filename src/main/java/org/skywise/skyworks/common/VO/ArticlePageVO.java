package org.skywise.skyworks.common.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/23 星期二 14:59
 * @Description:
 */
@Data
public class ArticlePageVO extends PageVO {
    List<ArticleVO> articleList;
}
