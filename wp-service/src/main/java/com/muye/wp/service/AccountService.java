package com.muye.wp.service;

import com.muye.wp.dao.domain.Account;

/**
 * Created by muye on 18/4/9.
 */
public interface AccountService {

    Account queryByUserIdForUpdate(Long userId);

    Account queryByUserId(Long userId);

    void add(Account account);

    void update(Account account);

    //是否支付过账户押金
    boolean isPaidCash(Long userId);

    //支付账户押金  返回支付订单号
    String payCash(Long userId);
}
