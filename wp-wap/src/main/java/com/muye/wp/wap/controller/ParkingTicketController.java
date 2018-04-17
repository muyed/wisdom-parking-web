package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.service.ParkingTicketService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Auth(UserType.GENERAL)
    @GetMapping("/pay/{id}")
    public Result<String> pay(@PathVariable Long id){
        return Result.ok(parkingTicketService.pay(SecurityConfig.getLoginId(), id));
    }

    @Auth(UserType.GENERAL)
    @GetMapping("/payOverdue/{id}")
    public Result<String> payOverdue(@PathVariable Long id){
        return Result.ok(parkingTicketService.payOverdue(SecurityConfig.getLoginId(), id));
    }
}
