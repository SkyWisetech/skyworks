package org.skywise.skyworks.common.VO;

import lombok.Data;
import org.skywise.skyworks.model.Article;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/23 星期二 15:55
 * @Description: 
 */
@Data
public class CollectPageVO extends PageVO{
    private List<Article> articleList;
}
