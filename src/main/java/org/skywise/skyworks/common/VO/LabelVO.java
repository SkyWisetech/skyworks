package org.skywise.skyworks.common.VO;

import lombok.Data;
import org.skywise.skyworks.model.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/24 星期三 16:52
 * @Description:
 */
@Data
public class LabelVO extends Label{

    List<Label> childLabelList = new ArrayList<>();
}
