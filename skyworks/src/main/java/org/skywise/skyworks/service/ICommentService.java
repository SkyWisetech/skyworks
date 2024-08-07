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

    CommentPageVO listComment(CommentOptDTO commentOptDTO);

    ArticleCommentPageVO listArticleComment(CommentOptDTO commentOptDTO);

    void deleteComment(CommentDTO commentDTO);

    void insertComment(CommentDTO commentDTO);
}
