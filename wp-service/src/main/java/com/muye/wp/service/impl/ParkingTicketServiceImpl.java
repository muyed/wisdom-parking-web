package com.muye.wp.service.impl;

import com.muye.wp.common.cons.*;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.common.utils.DateUtil;
import com.muye.wp.dao.domain.*;
import com.muye.wp.dao.mapper.ParkingTicketMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.*;
import com.muye.wp.listener.TicketPayListener;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 18/4/9.
 */
@Service
public class ParkingTicketServiceImpl implements ParkingTicketService {

    @Autowired(required = false)
    private ParkingTicketMapper parkingTicketMapper;

    @Autowired
    private ParkingShareService parkingShareService;

    @Autowired
    private UserCommunityService userCommunityService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TicketPayListener ticketPayListener;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Override
    public void add(ParkingTicket ticket) {
        parkingTicketMapper.insert(ticket);
    }

    @Override
    public ParkingTicket queryByIdForUpdate(Long id) {
        return parkingTicketMapper.selectByIdForUpdate(id);
    }

    @Override
    public ParkingTicket queryByTicketNum(String ticketNum) {
        return parkingTicketMapper.selectByTicketNum(ticketNum);
    }

    @Override
    public ParkingTicket queryByTicketNumForUpdate(String ticketNum) {
        return parkingTicketMapper.selectByTicketNumForUpdate(ticketNum);
    }

    @Override
    public List<ParkingTicket> queryListByCondition(ParkingTicket query, Page page) {
        return parkingTicketMapper.selectListByCondition(query, page);
    }

    @Override
    public void update(ParkingTicket ticket) {
        parkingTicketMapper.update(ticket);
    }

    @Override
    @Transactional
    public void matching(ParkingTicket ticket) {

        if (!accountService.isPaidCash(ticket.getUserId()))
            throw new WPException(RespStatus.BUSINESS_ERR, "您还没有支付账户押金");

        ParkingTicket noPaidQuery = new ParkingTicket();
        noPaidQuery.setUserId(ticket.getUserId());
        noPaidQuery.setStatus(ParkingTicketStatus.OVERDUE_UNPAID.getStatus());
        if (!CollectionUtils.isEmpty(queryListByCondition(noPaidQuery, null)))
            throw new WPException(RespStatus.BUSINESS_ERR, "你有未支付的逾期停车单未支付，请先支付");

        ParkingShare share = parkingShareService.queryByIdForUpdate(ticket.getParkingShareId());
        if (share == null) throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        if (share.getStatus() != ParkingShareStatus.MATCH.getStatus())
            throw new WPException(RespStatus.BUSINESS_ERR, "该共享单已经被匹配了");
        if (ticket.getAppointmentStartTime().before(share.getStartTime()))
            throw new WPException(RespStatus.BUSINESS_ERR, "停车单开始时间必须在共享单开始时间之后");
        if (ticket.getAppointmentEndTime().after(share.getStopTime()))
            throw new WPException(RespStatus.BUSINESS_ERR, "停车单结束时间必须在共享单结束时间之前");

        UserCommunity query = new UserCommunity();
        query.setUserId(ticket.getUserId());
        query.setType(UserCommunityType.PASS.getType());
        List<UserCommunity> userCommunityList = userCommunityService.queryByCondition(query, null);
        if (share.getCommunityType().equals(CommunityType.CLOSE.getType())
                && !userCommunityList.stream().anyMatch(userCommunity -> userCommunity.getCommunityId().equals(share.getCommunityId())))
            throw new WPException(RespStatus.BUSINESS_ERR, "你不是该小区业主，不能预约该共享单");

        //预约停车时长
        int parkingHours = DateUtil.ceilHours(ticket.getAppointmentStartTime(), ticket.getAppointmentEndTime());
        if (parkingHours < 0)
            throw new WPException(RespStatus.BUSINESS_ERR, "预约时间非法");

        ticket.setCommunityId(share.getCommunityId());
        ticket.setCarportId(share.getCarportId());
        ticket.setTicketNum(CommonUtil.genPayNum(ProductType.PARKING_TICKET));
        ticket.setStatus(ParkingTicketStatus.UNPAID.getStatus());
        ticket.setPayDeadlineTime(Date.from(LocalDateTime.now().plusMinutes(SysConfig.PAY_DEADLINE_MIN).atZone(ZoneId.systemDefault()).toInstant()));
        ticket.setPrice(share.getPrice());
        ticket.setParkingFee(share.getPrice().multiply(new BigDecimal(parkingHours)));
        ticket.setCarportNum(share.getCarportNum());
        ticket.setCarportMeid(share.getCarportMeid());
        ticket.setProvince(share.getProvince());
        ticket.setCity(share.getCity());
        ticket.setArea(share.getArea());
        ticket.setOpenCode(CommonUtil.genRandomNum(4));
        if (ticket.getPhone() == null){
            ticket.setPhone(userService.queryById(ticket.getUserId()).getPhone());
        }

        add(ticket);

        share.setParkingTicketId(ticket.getId());
        share.setStatus(ParkingShareStatus.UNPAID.getStatus());
        parkingShareService.update(share);

        //监听支付
        ticketPayListener.listen(ticket);
    }

    @Override
    @Transactional
    public void cancel(String ticketNum) {
        ParkingTicket ticket = queryByTicketNumForUpdate(ticketNum);
        if (ticket == null)
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        if (!ticket.getStatus().equals(ParkingTicketStatus.UNPAID.getStatus()))
            throw new WPException(RespStatus.BUSINESS_ERR, "只能取消待支付中的停车单");

        ParkingShare share = parkingShareService.queryByIdForUpdate(ticket.getParkingShareId());

        share.setStatus(ParkingShareStatus.MATCH.getStatus());
        share.setParkingTicketId(null);

        ticket.setStatus(ParkingTicketStatus.CANCEL.getStatus());

        update(ticket);
        parkingShareService.update(share);
    }

    @Override
    public String pay(Long userId, Long id) {
        ParkingTicket ticket = queryByIdForUpdate(id);
        if (ticket == null || !ticket.getUserId().equals(userId))
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);

        if (!ticket.getStatus().equals(ParkingTicketStatus.UNPAID.getStatus()))
            throw new WPException(RespStatus.BUSINESS_ERR, "该停车单状态不是待支付");

        if (capitalFlowService.queryByOrderNum(ticket.getTicketNum()) == null){
            capitalFlowService.add(userId, CapitalFlowDirection.OUT, ProductType.PARKING_TICKET, ticket.getTicketNum(), ticket.getParkingFee());
        }

        return ticket.getTicketNum();
    }

    @Override
    public String payOverdue(Long userId, Long id) {
        ParkingTicket ticket = queryByIdForUpdate(id);
        if (ticket == null || !ticket.getUserId().equals(userId))
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);

        if (!ticket.getStatus().equals(ParkingTicketStatus.OVERDUE_UNPAID.getStatus()))
            throw new WPException(RespStatus.BUSINESS_ERR, "该停车单状态不是逾期完成未支付");

        String payNum = ticket.getTicketNum().replace(ProductType.PARKING_TICKET.getCode(), ProductType.PARKING_TICKET_OVERDUE.getCode());
        if (capitalFlowService.queryByOrderNum(payNum) == null){
            capitalFlowService.add(userId, CapitalFlowDirection.OUT, ProductType.PARKING_TICKET_OVERDUE, payNum, ticket.getOverdueFee());
        }
        return payNum;
    }

    @Override
    @Transactional
    public void payToAccount(ParkingTicket ticket) {
        int status = ticket.getStatus().intValue();
        if (status != ParkingTicketStatus.FINISH.getStatus().intValue()
                && status != ParkingTicketStatus.OVERDUE_UNPAID.getStatus().intValue()
                && status != ParkingTicketStatus.OVERDUE_PAID.getStatus().intValue())
            throw new WPException(RespStatus.BUSINESS_ERR, "未完成的停车单不能支付");

        String flowNum = ticket.getTicketNum() + "A";
        if (capitalFlowService.queryByOrderNum(flowNum) != null) return;

        CapitalFlow capitalFlow = new CapitalFlow();
        capitalFlow.setUserId(parkingShareService.queryByIdForUpdate(ticket.getParkingShareId()).getUserId());
        capitalFlow.setDirection(CapitalFlowDirection.IN.getDirection());
        capitalFlow.setType(ProductType.PARKING_TICKET.getType());
        capitalFlow.setOrderNum(flowNum);
        capitalFlow.setAmount(ticket.getParkingFee().add(ticket.getOverdueFee()));
        capitalFlow.setStatus(CapitalFlowStatus.SUCCEED.getStatus());
        capitalFlowService.add(capitalFlow);

        Account account = accountService.queryByUserIdForUpdate(capitalFlow.getUserId());
        account.setBalance(account.getBalance().add(capitalFlow.getAmount()));
        accountService.update(account);
    }
}
