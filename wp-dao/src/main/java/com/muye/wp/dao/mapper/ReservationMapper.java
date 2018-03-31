package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.Reservation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by muye on 18/2/15.
 */
public interface ReservationMapper {

    @Select("select * from reservation where reservation_num = #{reservationNum}")
    Reservation selectByReservationNum(@Param("reservationNum") String reservationNum);

    @Update("update reservation set " +
            "status = #{reservation.status}, " +
            "parking_start_time = #{reservation.parkingStartTime}, " +
            "parking_end_time = #{reservation.parkingEndTime}, " +
            "long_overdue = #{reservation.longOverdue}, " +
            "tip_overdue_time = #{reservation.tipOverdueTime}, " +
            "overdue_amount = #{reservation.overdueAmount} " +
            "where reservation_num = #{reservation.reservationNum}")
    int update(@Param("reservation") Reservation reservation);
}
