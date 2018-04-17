package com.muye.wp.service.impl;

import com.muye.wp.common.cons.*;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.common.utils.DateUtil;
import com.muye.wp.dao.domain.*;
import com.muye.wp.dao.mapper.CarportMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.embed.server.door.service.DoorEmbedService;
import com.muye.wp.embed.server.lock.service.LockEmbedService;
import com.muye.wp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 18/3/6.
 */
@Service
public class CarportServiceImpl implements CarportService {

    @Autowired(required = false)
    private CarportMapper carportMapper;

    @Autowired
    private CommunityModuleService communityModuleService;

    @Autowired
    private ParkingTicketService parkingTicketService;

    @Autowired
    private ParkingShareService parkingShareService;

    @Autowired
    private LockEmbedService lockEmbedService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCarportService userCarportService;

    @Autowired
    private DoorEmbedService doorEmbedService;

    @Override
    public void add(Carport carport) {

        Carport query = new Carport();
        query.setMeid(carport.getMeid());

        if (carportMapper.selectListByCondition(query, null).size() > 0)
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        CommunityModule module = communityModuleService.queryById(carport.getCommunityModuleId());
        if (module == null){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST, "小区模块不存在");
        }
        carport.setCommunityId(module.getCommunityId());
        carport.setBindCode(CommonUtil.genRandomNum(6));
        carport.setLockStatus(0);
        carport.setShareStatus(0);
        carportMapper.insert(carport);
    }

    @Override
    @Transactional
    public String refreshBindCode(Long id) {

        Carport carport = carportMapper.selectByIdForUpdate(id);
        if (carport == null){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        }
        String bindCode = CommonUtil.genRandomNum(6);
        carport.setBindCode(bindCode);
        carportMapper.update(carport);
        return bindCode;
    }

    @Override
    public List<Carport> queryListByCondition(Carport query, Page page) {
        return carportMapper.selectListByCondition(query, page);
    }

    @Override
    public List<Carport> queryListByUserIdAndCommunityId(Long userId, Long communityId) {
        return carportMapper.selectListByUserIdAndCommunityId(userId, communityId);
    }

    @Override
    public Carport queryById(Long id) {
        return carportMapper.selectById(id);
    }

    @Override
    public void update(Carport carport) {
        carportMapper.update(carport);
    }

    @Override
    @Transactional
    public void lock(Long userId, Long carportId) {

        User user = userService.queryById(userId);
        Carport carport = queryById(carportId);

        //不是自己的锁判读是否为停车单的锁  是停车单的锁则走停车单结束流程
        if (UserType.GENERAL.getType() == user.getType().intValue() && !userCarportService.isPossess(userId, carportId)){
            ParkingTicket query = new ParkingTicket();
            query.setUserId(userId);
            query.setCarportId(carportId);
            query.setStatus(ParkingTicketStatus.PARKING.getStatus());
            List<ParkingTicket> ticketList = parkingTicketService.queryListByCondition(query, null);
            if (CollectionUtils.isEmpty(ticketList))
                throw new WPException(RespStatus.BUSINESS_ERR, "无权操作该车锁");
            ParkingTicket ticket = ticketList.get(0);
            ticket.setEndTime(new Date());

            int overdue = DateUtil.ceilHours(ticket.getAppointmentEndTime(), ticket.getEndTime());
            ticket.setOverdue(overdue < 0 ? 0 : overdue);
            ticket.setOverdueFee(ticket.getPrice().multiply(SysConfig.OVERDUE_MULTIPLE).multiply(new BigDecimal(ticket.getOverdue())));
            ticket.setStatus(ticket.getOverdue() == 0 ? ParkingTicketStatus.FINISH.getStatus() : ParkingTicketStatus.OVERDUE_UNPAID.getStatus());
            parkingTicketService.update(ticket);

            parkingTicketService.payToAccount(ticket);

            carport.setShareStatus(CarportShareStatus.UN_PUBLISH.getStatus());

            ParkingShare share = parkingShareService.queryByIdForUpdate(ticket.getParkingShareId());
            share.setStatus(ParkingShareStatus.FINISH.getStatus());
            parkingShareService.update(share);

            doorEmbedService.delTempLicense(carport.getCommunityId(), ticket.getId());

        }
        //自有锁或运营物业操作是判断有没有停车单正在停车
        else {
            ParkingTicket query = new ParkingTicket();
            query.setCarportId(carportId);
            query.setStatus(ParkingTicketStatus.PARKING.getStatus());
            List<ParkingTicket> ticketList = parkingTicketService.queryListByCondition(query, null);
            if (!CollectionUtils.isEmpty(ticketList))
                throw new WPException(RespStatus.BUSINESS_ERR, "共享单正在停车中，不能关锁");
        }

        if (lockEmbedService.lock(carport.getMeid())){
            carport.setLockStatus(CarportLockStatus.LOCK.getStatus());
            update(carport);
        }else {
            throw new WPException(RespStatus.EMBED_SERVER_ERR, "关锁失败");
        }
    }

    @Override
    public void unLock(Long userId, Long carportId){

        User user = userService.queryById(userId);
        Carport carport = queryById(carportId);
        if (UserType.GENERAL.getType() == user.getType().intValue() && !userCarportService.isPossess(userId, carportId)){
            ParkingTicket query = new ParkingTicket();
            query.setUserId(userId);
            query.setCarportId(carportId);
            query.setStatus(ParkingTicketStatus.PAID.getStatus());
            query.addRange().putColumn("appointment_start_time").putLE(new Date())
                    .and()
                    .addRange().putColumn("appointment_end_time").putGE(new Date());
            List<ParkingTicket> ticketList = parkingTicketService.queryListByCondition(query, null);
            if (CollectionUtils.isEmpty(ticketList))
                throw new WPException(RespStatus.BUSINESS_ERR, "无权操作该车锁");

            ParkingTicket ticket = ticketList.get(0);
            ticket.setStatus(ParkingTicketStatus.PARKING.getStatus());
            ticket.setStartTime(new Date());
            parkingTicketService.update(ticket);

            carport.setShareStatus(CarportShareStatus.SHARE.getStatus());

            doorEmbedService.delTempLicense(carport.getCommunityId(), ticket.getId());
        }

        if (lockEmbedService.unLock(carport.getMeid())){
            carport.setLockStatus(CarportLockStatus.OPEN.getStatus());
            update(carport);
        }else {
            throw new WPException(RespStatus.EMBED_SERVER_ERR, "开锁失败");
        }
    }
}
