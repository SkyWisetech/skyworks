package org.skywise.skyworks.controller;

import org.skywise.skyworks.common.DTO.CommentDTO;
import org.skywise.skyworks.common.DTO.CommentOptDTO;
import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.common.VO.ArticleCommentPageVO;
import org.skywise.skyworks.common.VO.CommentPageVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 14:27
 * @Description:
 */
@RestController
@RequestMapping("/skyworks/comment/")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @PostMapping("/listComment")
    public Response<CommentPageVO> listComment(@RequestBody CommentOptDTO commentOptDTO) {
        CommentPageVO commentPageVO = commentService.listComment(commentOptDTO);
        return Response.success(commentPageVO);
    }

    @PostMapping("/listArticleComment")
    public Response<ArticleCommentPageVO> listArticleComment(@RequestBody CommentOptDTO commentOptDTO) {
        ArticleCommentPageVO articleCommentPageVO = commentService.listArticleComment(commentOptDTO);
        return Response.success(articleCommentPageVO);
    }

    @PostMapping("/deleteComment")
    public Response<String> deleteComment(@RequestBody CommentDTO commentDTO) {
        commentService.deleteComment(commentDTO);
        return Response.successMsg(StrConstant.DELETE_COMMENT_SUCCESS);
    }

    @PostMapping("/insertComment")
    public Response<String> insertComment(@Valid @RequestBody CommentDTO commentDTO) {
        commentService.insertComment(commentDTO);
        return Response.successMsg(StrConstant.INSERT_COMMENT_SUCCESS);
    }
}
