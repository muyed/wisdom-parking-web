package com.muye.wp.service.impl;

import com.muye.wp.common.cons.*;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.dao.domain.Account;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.mapper.AccountMapper;
import com.muye.wp.service.AccountService;
import com.muye.wp.service.CapitalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 18/4/9.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired(required = false)
    private AccountMapper accountMapper;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Override
    public Account queryByUserIdForUpdate(Long userId) {
        return accountMapper.selectByUserIdForUpdate(userId);
    }

    @Override
    public Account queryByUserId(Long userId) {
        return accountMapper.selectByUserId(userId);
    }

    @Override
    public void add(Account account) {
        accountMapper.insert(account);
    }

    @Override
    public void update(Account account) {
        accountMapper.update(account);
    }

    @Override
    public boolean isPaidCash(Long userId) {
        Account account = queryByUserId(userId);
        if (account == null){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        }
        return  account.getCash().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public String payCash(Long userId) {
        if (isPaidCash(userId))
            throw new WPException(RespStatus.BUSINESS_ERR, "你已经支付过账户押金了");

        CapitalFlow query = new CapitalFlow();
        query.setUserId(userId);
        query.setType(ProductType.ACCOUNT_DEPOSIT.getType());
        query.setStatus(CapitalFlowStatus.ING.getStatus());
        List<CapitalFlow> flowList = capitalFlowService.queryListByCondition(query, null);
        if (!CollectionUtils.isEmpty(flowList))
            return flowList.get(0).getOrderNum();

        CapitalFlow flow = new CapitalFlow();
        flow.setUserId(userId);
        flow.setDirection(CapitalFlowDirection.IN.getDirection());
        flow.setType(ProductType.ACCOUNT_DEPOSIT.getType());
        flow.setOrderNum(CommonUtil.genPayNum(ProductType.ACCOUNT_DEPOSIT));
        flow.setAmount(SysConfig.ACCOUNT_CASH);
        flow.setStatus(CapitalFlowStatus.ING.getStatus());
        capitalFlowService.add(flow);

        return flow.getOrderNum();
    }
}
