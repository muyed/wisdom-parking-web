package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.service.ParkingTicketService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by muye on 18/4/9.
 */
@RestController
@RequestMapping("/api/ticket")
public class ParkingTicketController {

    @Autowired
    private ParkingTicketService parkingTicketService;

    @Auth(UserType.GENERAL)
    @PostMapping("/matching")
    public Result matching(@RequestBody ParkingTicket ticket){
        ticket.setUserId(SecurityConfig.getLoginId());
        parkingTicketService.matching(ticket);
        return Result.ok();
    }
}
