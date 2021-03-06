package com.muye.wp.service;

import com.muye.wp.dao.domain.UserCommunity;
import com.muye.wp.dao.domain.ext.UserCommunityVO;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/3/5.
 */
public interface UserCommunityService {

    /**
     * 用户发起认证为小区业主
     */
    void userAuth(UserCommunity userCommunity);

    /**
     * 审核用户发起的小区业主认证
     */
    void userAudit(Long id, Integer type);

    /**
     * 条件查询
     */
    List<UserCommunity> queryByCondition(UserCommunity query, Page page);

    /**
     * 判断车位是否在用户认证的小区内
     */
    Boolean isAtUserCommunity(Long carportId, Long userId);

    /**
     * 获取小区及小区内车位
     */
    List<UserCommunityVO> userCommunityVO(UserCommunity query, Page page);
}
