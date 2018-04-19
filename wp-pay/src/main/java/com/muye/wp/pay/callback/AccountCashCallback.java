package com.muye.wp.pay.callback;

import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.dao.domain.Account;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.service.AccountService;
import com.muye.wp.service.CapitalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by muye on 18/4/9.
 */
@Component("accountCashCallback")
public class AccountCashCallback implements Callback {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Override
    @Transactional
    public void finishedCallback(String orderNum) {
        CapitalFlow flow = capitalFlowService.queryByOrderNum(orderNum);
        Account account = accountService.queryByUserIdForUpdate(flow.getUserId());

        flow.setStatus(CapitalFlowStatus.SUCCEED.getStatus());
        account.setCash(account.getCash().add(flow.getAmount()));

        capitalFlowService.update(flow);
        accountService.update(account);
    }

    @Override
    public void successCallback(String orderNum) {
        finishedCallback(orderNum);
    }

    @Override
    public void closeCallback(String orderNum) {
        CapitalFlow flow = capitalFlowService.queryByOrderNum(orderNum);
        flow.setStatus(CapitalFlowStatus.FAILED.getStatus());
        capitalFlowService.update(flow);
    }
}
