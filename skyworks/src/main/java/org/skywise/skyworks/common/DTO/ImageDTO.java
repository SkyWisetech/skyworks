package org.skywise.skyworks.common.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: LIGHT
 * @Date: 2024/7/11 星期四 13:08
 * @Description:
 */
@Data
public class ImageDTO {
    MultipartFile image;
}
