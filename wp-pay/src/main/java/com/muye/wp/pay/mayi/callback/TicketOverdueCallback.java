package com.muye.wp.pay.mayi.callback;

import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.ParkingTicketStatus;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.service.CapitalFlowService;
import com.muye.wp.service.ParkingTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by muye on 18/4/17.
 */
@Component("ticketOverdueCallback")
public class TicketOverdueCallback implements MayiCallback {

    @Autowired
    private ParkingTicketService parkingTicketService;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Override
    @Transactional
    public void finishedCallback(String orderNum) {

        String ticketNum = orderNum.replace(ProductType.PARKING_TICKET_OVERDUE.getCode(), ProductType.PARKING_TICKET.getCode());
        ParkingTicket ticket = parkingTicketService.queryByTicketNumForUpdate(ticketNum);
        CapitalFlow flow = capitalFlowService.queryByOrderNum(orderNum);

        ticket.setStatus(ParkingTicketStatus.OVERDUE_PAID.getStatus());
        flow.setStatus(CapitalFlowStatus.SUCCEED.getStatus());

        parkingTicketService.update(ticket);
        capitalFlowService.update(flow);
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
