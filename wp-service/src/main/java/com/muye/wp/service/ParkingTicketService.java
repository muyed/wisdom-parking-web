package com.muye.wp.service;

import com.muye.wp.dao.domain.ParkingTicket;

/**
 * Created by muye on 18/4/9.
 */
public interface ParkingTicketService {

    void add(ParkingTicket ticket);

    ParkingTicket queryByTicketNum(String ticketNum);

    void matching(ParkingTicket ticket);
}
