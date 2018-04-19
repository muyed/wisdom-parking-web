package com.muye.wp.pay.callback;

import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.ParkingShareStatus;
import com.muye.wp.common.cons.ParkingTicketStatus;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.domain.ParkingShare;
import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.embed.mode.TempCarLicense;
import com.muye.wp.embed.server.door.service.DoorEmbedService;
import com.muye.wp.listener.TicketPayListener;
import com.muye.wp.service.CapitalFlowService;
import com.muye.wp.service.ParkingShareService;
import com.muye.wp.service.ParkingTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by muye on 18/4/11.
 */
@Component("ticketPayCallback")
public class TicketPayCallback implements Callback {

    @Autowired
    private ParkingTicketService parkingTicketService;

    @Autowired
    private ParkingShareService parkingShareService;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Autowired
    private TicketPayListener ticketPayListener;

    @Autowired
    private DoorEmbedService doorEmbedService;

    @Override
    @Transactional
    public void finishedCallback(String orderNum) {

        ParkingTicket ticket = parkingTicketService.queryByTicketNumForUpdate(orderNum);

        ticketPayListener.unListen(ticket);

        ParkingShare share = parkingShareService.queryByIdForUpdate(ticket.getParkingShareId());
        CapitalFlow flow = capitalFlowService.queryByOrderNum(orderNum);

        ticket.setStatus(ParkingTicketStatus.PAID.getStatus());
        share.setStatus(ParkingShareStatus.PAID.getStatus());
        flow.setStatus(CapitalFlowStatus.SUCCEED.getStatus());

        parkingTicketService.update(ticket);
        parkingShareService.update(share);
        capitalFlowService.update(flow);

        TempCarLicense tempCarLicense = new TempCarLicense();
        tempCarLicense.setId(ticket.getId());
        tempCarLicense.setCarLicense(ticket.getCarLicense());

        doorEmbedService.addTempLicense(ticket.getCommunityId(), tempCarLicense);
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
