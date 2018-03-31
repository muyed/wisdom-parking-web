package com.muye.wp.pay.mayi.callback;

import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.UserCarportStatus;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.service.CapitalFlowService;
import com.muye.wp.service.UserCarportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by muye on 18/3/31.
 */
@Component("carportCallback")
public class CarportCallback implements MayiCallback{

    @Autowired
    private UserCarportService userCarportService;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Override
    @Transactional
    public void finishedCallback(String orderNum) {
        UserCarport userCarport = userCarportService.queryByPayNum(orderNum);
        CapitalFlow capitalFlow = capitalFlowService.queryByOrderNum(orderNum);

        userCarport.setStatus(UserCarportStatus.PAID.getStatus());
        capitalFlow.setStatus(CapitalFlowStatus.SUCCEED.getStatus());

        userCarportService.update(userCarport);
        capitalFlowService.update(capitalFlow);
    }

    @Override
    @Transactional
    public void successCallback(String orderNum) {
        finishedCallback(orderNum);
    }

    @Override
    @Transactional
    public void closeCallback(String orderNum) {
        UserCarport userCarport = userCarportService.queryByPayNum(orderNum);
        CapitalFlow capitalFlow = capitalFlowService.queryByOrderNum(orderNum);

        userCarport.setStatus(UserCarportStatus.PAY_FAILED.getStatus());
        capitalFlow.setStatus(CapitalFlowStatus.FAILED.getStatus());

        userCarportService.update(userCarport);
        capitalFlowService.update(capitalFlow);
    }
}
