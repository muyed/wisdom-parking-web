package com.muye.wp.service.impl;

import com.muye.wp.dao.domain.Reservation;
import com.muye.wp.dao.mapper.ReservationMapper;
import com.muye.wp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by muye on 18/2/15.
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public Reservation selectByReservationNum(String reservationNum) {
        return reservationMapper.selectByReservationNum(reservationNum);
    }

    @Override
    public int update(Reservation reservation) {
        return reservationMapper.update(reservation);
    }
}
