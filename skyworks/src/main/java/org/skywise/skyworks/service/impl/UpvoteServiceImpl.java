package org.skywise.skyworks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.skywise.skyworks.common.DTO.UpvoteOptDTO;
import org.skywise.skyworks.common.enums.UpvoteEnum;
import org.skywise.skyworks.mapper.UpvoteMapper;
import org.skywise.skyworks.model.Upvote;
import org.skywise.skyworks.service.IUpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 16:46
 * @Description:
 */
@Service
public class UpvoteServiceImpl implements IUpvoteService {
    @Autowired
    private UpvoteMapper upvoteMapper;

    @Override
    public Long getUpvoteCount() {
        QueryWrapper<Upvote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Upvote.TYPE_COLUMN, UpvoteEnum.ARTICLE.getCode());
        return upvoteMapper.selectCount(queryWrapper);
    }

    @Override
    public List<Upvote> listUpvote(UpvoteOptDTO upvoteOptDTO) {
        LambdaQueryWrapper<Upvote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(upvoteOptDTO.getType() != null, Upvote::getType, upvoteOptDTO.getType());
        return upvoteMapper.selectList(queryWrapper);
    }

    @Override
    public void insertUpvoteList(List<Upvote> upvoteList) {
        upvoteMapper.insertBatch(upvoteList);
    }
}
