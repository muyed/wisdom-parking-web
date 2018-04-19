package com.muye.wp.service.impl;

import com.muye.wp.common.cons.CapitalFlowDirection;
import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.DateUtil;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.dao.mapper.CapitalFlowMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CapitalFlowService;
import com.muye.wp.service.ParkingTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 18/3/30.
 */
@Service
public class CapitalFlowServiceImpl implements CapitalFlowService {

    @Autowired(required = false)
    private CapitalFlowMapper capitalFlowMapper;

    @Autowired
    private ParkingTicketService parkingTicketService;

    @Override
    public List<CapitalFlow> queryListByCondition(CapitalFlow query, Page page) {
        return capitalFlowMapper.selectListByCondition(query, page);
    }

    @Override
    public void add(CapitalFlow flow) {
        capitalFlowMapper.insert(flow);
    }

    @Override
    public void add(Long userId, CapitalFlowDirection direction, ProductType type, String orderNum, BigDecimal amount) {
        CapitalFlow capitalFlow = new CapitalFlow();
        capitalFlow.setUserId(userId);
        capitalFlow.setDirection(direction.getDirection());
        capitalFlow.setType(type.getType());
        capitalFlow.setOrderNum(orderNum);
        capitalFlow.setAmount(amount);
        capitalFlow.setStatus(CapitalFlowStatus.ING.getStatus());
        add(capitalFlow);
    }

    @Override
    public CapitalFlow queryByOrderNum(String orderNum) {
        return capitalFlowMapper.selectByOrderNum(orderNum);
    }

    @Override
    public void update(CapitalFlow flow) {
        capitalFlowMapper.update(flow);
    }

    @Override
    public int getTimeoutExpress(CapitalFlow flow) {

        int timeoutExpress = 30;
        //停车单需设置过期时间
        if (flow.getType().equals(ProductType.PARKING_TICKET.getType())){
            ParkingTicket ticket = parkingTicketService.queryByTicketNum(flow.getOrderNum());
            timeoutExpress = DateUtil.betweenMin(ticket.getPayDeadlineTime(), new Date()) * -1;
            if (timeoutExpress < 0)
                throw new WPException(RespStatus.PAY_GEN_INFO_FAIL, "订单已过期");
            if (timeoutExpress == 0)
                timeoutExpress = 1;
        }

        return timeoutExpress;
    }
}
