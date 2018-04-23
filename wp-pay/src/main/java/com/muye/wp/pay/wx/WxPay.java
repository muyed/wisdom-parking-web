package com.muye.wp.pay.wx;

import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.domain.UserBank;
import com.muye.wp.service.CapitalFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by muye on 18/4/19.
 */
@Component
public class WxPay {

    private static final Logger logger = LoggerFactory.getLogger(WxPay.class);

    @Autowired
    private CapitalFlowService capitalFlowService;

    public Map<String, String> genPayInfo(String orderNum){

        CapitalFlow flow = capitalFlowService.queryByOrderNum(orderNum);

        if (flow == null) throw new WPException(RespStatus.RESOURCE_NOT_EXIST);

        if (!flow.getStatus().equals(CapitalFlowStatus.ING.getStatus()))
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL, "订单已过期");

        int timeoutExpress = capitalFlowService.getTimeoutExpress(flow);
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(timeoutExpress);

        return WxPayUtil.payInfo(flow, endTime);
    }

    public void withdraw(UserBank userBank, BigDecimal amount, CapitalFlow flow){

        if (amount.intValue() <= 0) throw new WPException(RespStatus.BUSINESS_ERR, "余额不足");

        ProductType type = ProductType.ofType(flow.getType());
        String encBankNo = userBank.getBankAccount();
        String encTrueName = userBank.getAccountName();
        Integer bankCode = userBank.getBankCode();
        WxPayUtil.withdraw(encBankNo, encTrueName, bankCode, amount, flow.getOrderNum(), type.getName());
    }
}
