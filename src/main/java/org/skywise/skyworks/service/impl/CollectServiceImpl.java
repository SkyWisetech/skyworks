package org.skywise.skyworks.service.impl;

import org.skywise.skyworks.common.DTO.CollectOptDTO;
import org.skywise.skyworks.mapper.CollectMapper;
import org.skywise.skyworks.model.Collect;
import org.skywise.skyworks.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 14:06
 * @Description:
 */
@Service
public class CollectServiceImpl implements ICollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Override
    public List<Collect> listCollect(CollectOptDTO collectOptDTO) {
        return collectMapper.selectList(null);
    }

    @Override
    public void insertBatch(List<Collect> collectList) {
        collectMapper.insertBatch(collectList);
    }
}
