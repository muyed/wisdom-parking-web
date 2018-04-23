package com.muye.wp.service;

import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.domain.ext.UserCarportExt;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/3/15.
 */
public interface UserCarportService {

    List<UserCarport> queryByCondition(UserCarport query, Page page);

    UserCarport queryById(Long id);

    UserCarport queryByIdForUpdate(Long id);

    UserCarport queryByPayNum(String payNum);

    void update(UserCarport userCarport);

    /**
     * 用户绑定车位 返回支付单信息
     */
    String bind(UserCarportExt userCarport);

    void changeAlias(Long userId, Long userCarportId, String alias);

    /**
     *  用户是否持有指定车锁
     */
    boolean isPossess(Long userId, Long carportId);
}
