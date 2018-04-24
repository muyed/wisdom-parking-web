package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.CapitalFlowDirection;
import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.dao.domain.Account;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.domain.UserBank;
import com.muye.wp.pay.wx.WxPay;
import com.muye.wp.service.AccountService;
import com.muye.wp.service.CapitalFlowService;
import com.muye.wp.service.UserBankService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Created by muye on 18/4/9.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Autowired
    private WxPay wxPay;

    @Auth(value = UserType.GENERAL, cret = true)
    @GetMapping("/payCash")
    public Result<String> payCash(){
        return Result.ok(accountService.payCash(SecurityConfig.getLoginId()));
    }

    @Auth(UserType.GENERAL)
    @PostMapping("/withdrawCash")
    @Transactional
    public Result withdrawCash(@RequestBody UserBank userBank){

        userBank = userBankService.queryById(userBank.getId());
        Account account = accountService.queryByUserId(SecurityConfig.getLoginId());
        CapitalFlow flow = new CapitalFlow();
        flow.setUserId(account.getUserId());
        flow.setDirection(CapitalFlowDirection.OUT.getDirection());
        flow.setType(ProductType.WITHDRAW_ACCOUNT_DEPOSIT.getType());
        flow.setOrderNum(CommonUtil.genPayNum(ProductType.WITHDRAW_ACCOUNT_DEPOSIT));
        flow.setAmount(account.getCash());
        flow.setStatus(CapitalFlowStatus.SUCCEED.getStatus());
        capitalFlowService.add(flow);
        wxPay.withdraw(userBank, account.getCash(), flow);
        account.setCash(BigDecimal.ZERO);
        accountService.update(account);

        return Result.ok();
    }
}
