package org.skywise.skyworks.service;

import org.skywise.skyworks.common.DTO.UpvoteOptDTO;
import org.skywise.skyworks.model.Upvote;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 16:45
 * @Description:
 */
public interface IUpvoteService {
    Long getUpvoteCount();

    List<Upvote> listUpvote(UpvoteOptDTO upvoteOptDTO);

    void insertUpvoteList(List<Upvote> upvoteList);
}
