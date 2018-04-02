package com.muye.wp.service;

import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.domain.ext.UserCarportExt;
import com.muye.wp.dao.domain.query.UserCarportQuery;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/3/15.
 */
public interface UserCarportService {

    List<UserCarport> queryByCondition(UserCarportQuery query, Page page);

    UserCarport queryById(Long id);

    UserCarport queryByPayNum(String payNum);

    void update(UserCarport userCarport);

    /**
     * 用户绑定车位 返回payNum
     */
    String bind(UserCarportExt userCarport);

    void changeAlias(Long userId, Long userCarportId, String alias);
}
