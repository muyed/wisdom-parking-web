package com.muye.wp.service;

import com.muye.wp.dao.domain.Reservation;

/**
 * Created by muye on 18/2/15.
 */
public interface ReservationService {

    Reservation selectByReservationNum(String reservationNum);

    int update(Reservation reservation);
}
