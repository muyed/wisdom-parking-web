package com.muye.wp.service;

import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/4/9.
 */
public interface ParkingTicketService {

    void add(ParkingTicket ticket);

    ParkingTicket queryByIdForUpdate(Long id);

    ParkingTicket queryByTicketNum(String ticketNum);

    ParkingTicket queryByTicketNumForUpdate(String ticketNum);

    List<ParkingTicket> queryListByCondition(ParkingTicket query, Page page);

    void update(ParkingTicket ticket);

    void matching(ParkingTicket ticket);

    void cancel(String ticketNum);

    /**
     * 支付停车单 获取支付单号
     */
    String pay(Long userId, Long id);
}
