package com.muye.wp.service.impl;

import com.muye.wp.common.cons.Bank;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.User;
import com.muye.wp.dao.domain.UserBank;
import com.muye.wp.dao.mapper.UserBankMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.UserBankService;
import com.muye.wp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by muye on 18/4/23.
 */
@Service
public class UserBankServiceImpl implements UserBankService {

    @Autowired(required = false)
    private UserBankMapper userBankMapper;

    @Autowired
    private UserService userService;


    @Override
    public List<UserBank> queryListByCondition(UserBank query, Page page) {
        return userBankMapper.selectListByCondition(query, page);
    }

    @Override
    public UserBank queryById(Long id) {
        return userBankMapper.selectById(id);
    }

    @Override
    public void add(UserBank userBank) {
        userBank.setBankName(Bank.ofBankCode(userBank.getBankCode()).getBankName());

        User user = userService.queryById(userBank.getUserId());
        if (!user.getRealName().equals(userBank.getAccountName()))
            throw new WPException(RespStatus.BUSINESS_ERR, "绑定的银行卡必须是本人的银行卡");

        UserBank query = new UserBank();
        query.setUserId(userBank.getUserId());
        query.setBankAccount(userBank.getBankAccount());
        if (!CollectionUtils.isEmpty(queryListByCondition(query, null)))
            throw new WPException(RespStatus.BUSINESS_ERR, "你已绑定过该卡号了");

        userBankMapper.insert(userBank);
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        userBankMapper.deleteByIdAndUserId(id, userId);
    }
}
