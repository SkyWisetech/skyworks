package org.skywise.skyworks.service;

import org.skywise.skyworks.common.DTO.CollectOptDTO;
import org.skywise.skyworks.model.Collect;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 14:06
 * @Description:
 */
public interface ICollectService {
    List<Collect> listCollect(CollectOptDTO collectOptDTO);

    void insertBatch(List<Collect> collectList);
}
