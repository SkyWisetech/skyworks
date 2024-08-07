package org.skywise.skyworks.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.skywise.skyworks.common.DTO.LabelDTO;
import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.common.VO.LabelVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.exception.ServiceException;
import org.skywise.skyworks.model.Label;
import org.skywise.skyworks.service.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 16:42
 * @Description:
 */
@RestController
@RequestMapping("/skyworks/label/")
public class LabelController {
    @Autowired
    private ILabelService labelService;

    @PostMapping("/listLabel")
    public Response<List<LabelVO>> listLabel(@RequestBody LabelDTO labelDTO) {
        List<Label> labelList = labelService.listLabel(labelDTO);

        // 处理数据

        Map<Integer, LabelVO> labelMap = new HashMap<>();
        for (Label label : labelList) {
            if (label.getParentId() == 0) {
                // 添加一级标签
                LabelVO labelVO = new LabelVO();
                try {
                    BeanUtils.copyProperties(labelVO, label);
                } catch (Exception e) {
                    throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
                }
                labelMap.put(label.getId(), labelVO);
            } else {
                //添加二级标签
                LabelVO labelVO = labelMap.get(label.getParentId());
                labelVO.getChildLabelList().add(label);
            }
        }
        // 封装成二维数组
        List<LabelVO> labelVOList = new ArrayList<>(labelMap.values());

        // 一级标签，二级标签排序-按照优先级，数字越大，优先级越高
        labelVOList = labelVOList.stream().sorted(Comparator.comparing(LabelVO::getPriority).reversed()).collect(Collectors.toList());
        for (LabelVO labelVO : labelVOList) {
            List<Label> childList = labelVO.getChildLabelList().stream().sorted(Comparator.comparing(Label::getPriority).reversed()).collect(Collectors.toList());
            labelVO.setChildLabelList(childList);
        }

        return Response.success(labelVOList);
    }

    @PostMapping("/insertLabel")
    public Response<String> insertLabel(@RequestBody LabelDTO labelDTO) {
        labelService.insertLabel(labelDTO);
        return Response.successMsg(StrConstant.INSERT_LABEL_SUCCESS);
    }

    @PostMapping("/deleteLabel")
    public Response<String> deleteLabel(@RequestBody LabelDTO labelDTO) {
        labelService.deleteLabel(labelDTO);
        return Response.successMsg(StrConstant.DELETE_LABEL_SUCCESS);
    }

    @PostMapping("/updateLabel")
    public Response<String> updateLabel(@RequestBody LabelDTO labelDTO) {
        labelService.updateLabel(labelDTO);
        return Response.successMsg(StrConstant.UPDATE_LABEL_SUCCESS);
    }
}
