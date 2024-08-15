package org.skywise.skyworks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.skywise.skyworks.common.DTO.CommentDTO;
import org.skywise.skyworks.common.DTO.CommentOptDTO;
import org.skywise.skyworks.common.VO.ArticleCommentPageVO;
import org.skywise.skyworks.common.VO.ArticleCommentVO;
import org.skywise.skyworks.common.VO.CommentPageVO;
import org.skywise.skyworks.common.VO.CommentVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.DeleteEnum;
import org.skywise.skyworks.common.enums.ParentEnum;
import org.skywise.skyworks.common.exception.ServiceException;
import org.skywise.skyworks.common.utils.TokenUtil;
import org.skywise.skyworks.mapper.CommentMapper;
import org.skywise.skyworks.model.Article;
import org.skywise.skyworks.model.Comment;
import org.skywise.skyworks.service.IArticleService;
import org.skywise.skyworks.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 14:30
 * @Description: 评论管理
 */
@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IArticleService articleService;

    @Override
    public Long getCommentCount(CommentOptDTO commentOptDTO) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        // 邮箱、昵称查询
        queryWrapper.eq(commentOptDTO.getParentId() != null, Comment::getParentId, commentOptDTO.getParentId())
                .eq(commentOptDTO.getUserId() != null, Comment::getUserId, commentOptDTO.getUserId())
                .eq(Comment::getIsDelete, DeleteEnum.NOT_DELETE.getCode());
        return commentMapper.selectCount(queryWrapper);
    }

    @Override
    public CommentPageVO listComment(CommentOptDTO commentOptDTO) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(commentOptDTO.getArticleId() != null, Comment::getArticleId, commentOptDTO.getArticleId())
                .eq(commentOptDTO.getUserId() != null, Comment::getUserId, commentOptDTO.getUserId())
                .eq(Comment::getIsDelete, DeleteEnum.NOT_DELETE.getCode());
        // 设置分页
        PageHelper.startPage(commentOptDTO.getPageNum(), commentOptDTO.getPageSize());
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        // 过滤掉回复评论的评论，只显示文章评论
        commentList = commentList.stream().filter(Comment -> Comment.getParentId().equals(ParentEnum.IS_PARENT.getCode())).collect(Collectors.toList());
        List<Integer> idList = commentList.stream().map(Comment::getArticleId).collect(Collectors.toList());
        List<CommentVO> commentVOList = new ArrayList<>();
        if (!idList.isEmpty()) {
            List<Article> articleList = articleService.listArticleByIds(idList);
            for (Comment comment : commentList) {
                Article article = articleList.stream().filter(Article -> Article.getId().equals(comment.getArticleId())).findFirst().orElse(new Article());
                CommentVO commentVO = new CommentVO();
                commentVO.setArticleId(article.getId());
                commentVO.setTitle(article.getTitle());
                commentVO.setId(comment.getId());
                commentVO.setContent(comment.getContent());
                commentVO.setCreateTime(comment.getCreateTime());
                commentVOList.add(commentVO);
            }
        }

        // 输出分页信息
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        CommentPageVO commentPageVO = new CommentPageVO();
        commentPageVO.setCommentList(commentVOList);
        commentPageVO.setPageNum(pageInfo.getPageNum());
        commentPageVO.setPageSize(pageInfo.getPageSize());
        commentPageVO.setTotalCount(pageInfo.getTotal());
        commentPageVO.setTotalPage(pageInfo.getPages());
        return commentPageVO;
    }

    @Override
    public ArticleCommentPageVO listArticleComment(CommentOptDTO commentOptDTO) {
        // 设置分页
        PageHelper.startPage(commentOptDTO.getPageNum(), commentOptDTO.getPageSize());
        List<CommentVO> commentVOList = commentMapper.listArticleComment(commentOptDTO.getArticleId());

        // 处理评论数据
        Map<Integer, ArticleCommentVO> firstCommentMap = new HashMap<>();
        for (CommentVO commentVO : commentVOList) {
            if (commentVO.getParentId().equals(ParentEnum.IS_PARENT.getCode())) {
                ArticleCommentVO articleCommentVO = firstCommentMap.getOrDefault(commentVO.getId(), new ArticleCommentVO());
                try {
                    BeanUtils.copyProperties(articleCommentVO, commentVO);
                } catch (Exception e) {
                    throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
                }
                firstCommentMap.put(commentVO.getId(), articleCommentVO);
            } else {
                ArticleCommentVO articleCommentVO = firstCommentMap.getOrDefault(commentVO.getParentId(), new ArticleCommentVO());
                articleCommentVO.getChildCommentList().add(commentVO);
            }
        }

        // 封装返回参数
        ArticleCommentPageVO articleCommentPageVO = new ArticleCommentPageVO();
        List<ArticleCommentVO> articleCommentVOList = new ArrayList<>(firstCommentMap.values());
        PageInfo<CommentVO> pageInfo = new PageInfo<>(commentVOList);

        articleCommentPageVO.setArticleCommentList(articleCommentVOList);
        articleCommentPageVO.setPageNum(pageInfo.getPageNum());
        articleCommentPageVO.setPageSize(pageInfo.getPageSize());
        articleCommentPageVO.setTotalCount(pageInfo.getTotal());
        articleCommentPageVO.setTotalPage(pageInfo.getPages());
        return articleCommentPageVO;
    }

    @Override
    public void deleteComment(CommentDTO commentDTO) {
        UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(Comment.COMMENT_ID, commentDTO.getCommentId());
        updateWrapper.set(Comment.IS_DELETE, DeleteEnum.DELETE.getCode());
        int result = commentMapper.update(null, updateWrapper);
        if (result != 1) {
            throw new ServiceException(StrConstant.DELETE_COMMENT_FAIL);
        }

    }

    @Transactional
    @Override
    public void insertComment(CommentDTO commentDTO) {
        Integer userId = TokenUtil.getUserId();
        Comment comment = new Comment();
        comment.setUserId(userId);
        try {
            BeanUtils.copyProperties(comment, commentDTO);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        // 插入评论
        int result = commentMapper.insert(comment);
        if (result != 1) {
            throw new ServiceException(StrConstant.INSERT_COMMENT_SUCCESS);
        }

        // 修改文章的评论数量
        articleService.increaseCommentNum(commentDTO.getArticleId());
    }
}
