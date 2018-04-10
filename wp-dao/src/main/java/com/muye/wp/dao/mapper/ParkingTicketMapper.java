package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.ParkingTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 * Created by muye on 18/4/9.
 */
public interface ParkingTicketMapper {

    @Select("select * from parking_ticket where ticket_num = #{ticketNum}")
    ParkingTicket selectByTicketNum(@Param("ticketNum") String ticketNum);

    @Insert("insert into parking_ticket (user_id,community_id,carport_id,parking_share_id,ticket_num,status," +
            "appointment_start_time,appointment_end_time,start_time,end_time,pay_deadline_time,car_license,phone," +
            "price,parking_fee,overdue,overdue_fee,carport_num,carport_meid,province,city,area,open_code) values (" +
            "#{ticket.userId}," +
            "#{ticket.communityId}," +
            "#{ticket.carportId}," +
            "#{ticket.parkingShareId}," +
            "#{ticket.ticketNum}," +
            "#{ticket.status}," +
            "#{ticket.appointmentStartTime}," +
            "#{ticket.appointmentEndTime}," +
            "#{ticket.startTime}," +
            "#{ticket.endTime}," +
            "#{ticket.payDeadlineTime}," +
            "#{ticket.carLicense}," +
            "#{ticket.phone}," +
            "#{ticket.price}," +
            "#{ticket.parkingFee}," +
            "#{ticket.overdue}," +
            "#{ticket.overdueFee}," +
            "#{ticket.carportNum}," +
            "#{ticket.carportMeid}," +
            "#{ticket.province}," +
            "#{ticket.city}," +
            "#{ticket.area}," +
            "#{ticket.openCode})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "ticket.id", resultType = Long.class, before = false)
    int insert(@Param("ticket") ParkingTicket ticket);
}
