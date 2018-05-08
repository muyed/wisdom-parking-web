package com.muye.wp.service;

import com.muye.wp.dao.domain.UserBank;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/4/23.
 */
public interface UserBankService {

    List<UserBank> queryListByCondition(UserBank query, Page page);

    UserBank queryById(Long id);

    void add(UserBank userBank);

    void deleteByIdAndUserId(Long id, Long userId);
}
