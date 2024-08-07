package org.skywise.skyworks.service;

import org.skywise.skyworks.common.DTO.LabelDTO;
import org.skywise.skyworks.model.Label;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 17:01
 * @Description:
 */
public interface ILabelService {
    Label getLabelById(int id);

    List<Label> listLabel(LabelDTO labelDTO);

    List<Label> listLabelByIdList(List<Integer> idList);

    void insertLabel(LabelDTO labelDTO);

    void deleteLabel(LabelDTO labelDTO);

    void updateLabel(LabelDTO labelDTO);
}
