package org.skywise.skyworks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.skywise.skyworks.common.VO.CommentVO;
import org.skywise.skyworks.model.Comment;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 13:57
 * @Description:
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT com.id, com.upvote as upvoteNum, com.parent_id as parentId, com.user_id as userId, com.article_id as articleId, com.content, com.create_time as createTime, u.nick_name as userName FROM `comment` com LEFT JOIN `user` u ON com.user_id = u.id WHERE com.article_id = #{articleId} and com.is_delete = 0")
    List<CommentVO> listArticleComment(Integer articleId);
}
