package org.skywise.skyworks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.beanutils.BeanUtils;
import org.skywise.skyworks.common.DTO.ArticleOptDTO;
import org.skywise.skyworks.common.DTO.LabelDTO;
import org.skywise.skyworks.common.VO.ArticlePageVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.exception.ServiceException;
import org.skywise.skyworks.mapper.LabelMapper;
import org.skywise.skyworks.model.Label;
import org.skywise.skyworks.service.IArticleService;
import org.skywise.skyworks.service.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 17:01
 * @Description:
 */
@Service
public class LabelServiceImpl implements ILabelService {
    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private IArticleService articleService;

    @Resource
    private Cache<Integer, Set<Integer>> labelCache;

    @Resource
    private Cache<Integer, String> labelNameCache;

    @Override
    public Label getLabelById(int id) {
        return labelMapper.selectById(id);
    }

    @Override
    public List<Label> listLabel(LabelDTO labelDTO) {
        LambdaQueryWrapper<Label> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(labelDTO.getId() != null, Label::getId, labelDTO.getId())
                .eq(labelDTO.getParentId() != null, Label::getParentId, labelDTO.getParentId())
                .like(StringUtils.hasLength(labelDTO.getLabelName()), Label::getLabelName, labelDTO.getLabelName())
                .orderByAsc(Label::getParentId, Label::getPriority);
        return labelMapper.selectList(queryWrapper);
    }

    @Override
    public List<Label> listLabelByIdList(List<Integer> idList) {
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(Label.LABEL_ID, idList);
        return labelMapper.selectList(queryWrapper);
    }

    @Override
    public void insertLabel(LabelDTO labelDTO) {
        Label label = new Label();
        try {
            BeanUtils.copyProperties(label, labelDTO);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        int result = labelMapper.insert(label);
        if (result != 1) {
            throw new ServiceException(StrConstant.INSERT_LABEL_FAIL);
        }
    }

    @Override
    public void deleteLabel(LabelDTO labelDTO) {
        // 一级标签：如果下面还有二级标签，则不能删除
        // 二级标签：如果下面还有文章，则不能删除
        List<Label> labelList = this.listLabel(labelDTO);
        if (labelList == null || labelList.isEmpty()) {
            throw new ServiceException(StrConstant.DELETE_LABEL_FAIL_NOT_EXIST);
        } else if (labelList.get(0).getParentId() == 0) {
            // 一级标签：如果下面还有二级标签，则不能删除
            Set<Integer> secondLabelList = labelCache.getIfPresent(labelList.get(0).getId());
            if (secondLabelList != null && !secondLabelList.isEmpty()) {
                throw new ServiceException(StrConstant.DELETE_LABEL_FAIL_EXIST_SECOND_LABEL);
            }
        } else {
            ArticleOptDTO articleOptDTO = new ArticleOptDTO();
            articleOptDTO.setLabelId(labelDTO.getId());
            ArticlePageVO articlePageVO = articleService.listArticle(articleOptDTO);
            if (!articlePageVO.getArticleList().isEmpty()) {
                throw new ServiceException(StrConstant.DELETE_LABEL_FAIL_EXIST_ARTICLE);
            }
        }

        // 获取所有ID
        int result = labelMapper.deleteById(labelDTO.getId());
        if (result != 1) {
            throw new ServiceException(StrConstant.DELETE_LABEL_FAIL);
        }
    }

    @Override
    public void updateLabel(LabelDTO labelDTO) {
        Label label = new Label();
        try {
            BeanUtils.copyProperties(label, labelDTO);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        int result = labelMapper.updateById(label);
        if (result != 1) {
            throw new ServiceException(StrConstant.UPDATE_LABEL_FAIL);
        }

        // 同时修改缓存
        labelNameCache.put(labelDTO.getId(),labelDTO.getLabelName());

    }
}
