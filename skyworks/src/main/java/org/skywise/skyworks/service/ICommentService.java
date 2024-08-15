package org.skywise.skyworks.service;

import org.skywise.skyworks.common.DTO.CommentDTO;
import org.skywise.skyworks.common.DTO.CommentOptDTO;
import org.skywise.skyworks.common.VO.ArticleCommentPageVO;
import org.skywise.skyworks.common.VO.CommentPageVO;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 14:29
 * @Description:
 */
public interface ICommentService {
    Long getCommentCount(CommentOptDTO commentOptDTO);

    /**
     * 用户查询自己的评论文章的评论，屏蔽掉评论评论的条数
     * */
    CommentPageVO listComment(CommentOptDTO commentOptDTO);

    ArticleCommentPageVO listArticleComment(CommentOptDTO commentOptDTO);

    void deleteComment(CommentDTO commentDTO);

    void insertComment(CommentDTO commentDTO);
}
