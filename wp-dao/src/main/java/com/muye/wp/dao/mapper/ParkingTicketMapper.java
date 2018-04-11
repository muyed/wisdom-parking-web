package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/4/9.
 */
public interface ParkingTicketMapper {

    @Select("select * from parking_ticket where ticket_num = #{ticketNum}")
    ParkingTicket selectByTicketNum(@Param("ticketNum") String ticketNum);

    @Select("select * from parking_ticket where ticket_num = #{ticketNum} for update")
    ParkingTicket selectByTicketNumForUpdate(@Param("ticketNum") String ticket);

    @Select("select * from parking_ticket where id = #{id} for update")
    ParkingTicket selectByIdForUpdate(@Param("id") Long id);

    @Select("<script>" +
            "select * from parking_ticket where 1 = 1 " +
            "<if test='query.userId != null'>and user_id = #{query.userId}</if>" +
            "<if test='query.communityId != null'>and community_id = #{query.communityId}</if>" +
            "<if test='query.carportId != null'>and carport_id = #{query.carportId}</if>" +
            "<if test='query.parkingShareId != null'>and parking_share_id = #{query.parkingShareId}</if>" +
            "<if test='query.ticketNum != null'>and ticket_num = #{query.ticketNum}</if>" +
            "<if test='query.status != null'>and status = #{query.status}</if>" +
            "<if test='query.appointmentStartTime != null'>and appointment_start_time = #{query.appointmentStartTime}</if>" +
            "<if test='query.appointmentEndTime != null'>and appointment_end_time = #{query.appointmentEndTime}</if>" +
            "<if test='query.startTime != null'>and start_time = #{query.startTime}</if>" +
            "<if test='query.endTime != null'>and end_time = #{query.endTime}</if>" +
            "<if test='query.payDeadlineTime != null'>and pay_deadline_time = #{query.payDeadlineTime}</if>" +
            "<if test='query.carLicense != null'>and car_license = #{query.carLicense}</if>" +
            "<if test='query.phone != null'>and phone = #{query.phone}</if>" +
            "<if test='query.price != null'>and price = #{query.price}</if>" +
            "<if test='query.parkingFee != null'>and parking_fee = #{query.parkingFee}</if>" +
            "<if test='query.overdue != null'>and overdue = #{query.overdue}</if>" +
            "<if test='query.overdueFee != null'>and overdue_fee = #{query.overdueFee}</if>" +
            "<if test='query.carportNum != null'>and carport_num = #{query.carportNum}</if>" +
            "<if test='query.carportMeid != null'>and carport_meid = #{query.carportMeid}</if>" +
            "<if test='query.province != null'>and province = #{query.province}</if>" +
            "<if test='query.city != null'>and city = #{query.city}</if>" +
            "<if test='query.area != null'>and area = #{query.area}</if>" +
            "<if test='query.openCode != null'>and open_code = #{query.openCode}</if>" +
            "<if test='query.ranges != null'>" +
            "   <foreach collection='query.ranges' item='item'>" +
            "       <if test='item.GT != null'><![CDATA[and `${item.column}` > #{item.gt}]]></if>" +
            "       <if test='item.LT != null'><![CDATA[and `${item.column}` < #{item.lt}]]></if>" +
            "       <if test='item.GE != null'><![CDATA[and `${item.column}` >= #{item.ge}]]></if>" +
            "       <if test='item.LE != null'><![CDATA[and `${item.column}` <= #{item.le}]]></if>" +
            "   </foreach>" +
            "</if>" +
            "<if test='query.sorts != null'>" +
            "   order by " +
            "   <foreach collection='query.sorts' item='item' separator=','>`${item.column}` ${item.sort}</foreach>" +
            "</if>" +
            "</script>")
    List<ParkingTicket> selectListByCondition(@Param("query") ParkingTicket query, Page page);

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

    @Update("update parking_ticket set " +
            "user_id = #{ticket.userId}," +
            "community_id = #{ticket.communityId}," +
            "carport_id = #{ticket.carportId}," +
            "parking_share_id = #{ticket.parkingShareId}," +
            "ticket_num = #{ticket.ticketNum}," +
            "status = #{ticket.status}," +
            "appointment_start_time = #{ticket.appointmentStartTime}," +
            "appointment_end_time = #{ticket.appointmentEndTime}," +
            "start_time = #{ticket.startTime}," +
            "end_time = #{ticket.endTime}," +
            "pay_deadline_time = #{ticket.payDeadlineTime}," +
            "car_license = #{ticket.carLicense}," +
            "phone = #{ticket.phone}," +
            "price = #{ticket.price}," +
            "parking_fee = #{ticket.parkingFee}," +
            "overdue = #{ticket.overdue}," +
            "overdue_fee = #{ticket.overdueFee}," +
            "carport_num = #{ticket.carportNum}," +
            "carport_meid = #{ticket.carportMeid}," +
            "province = #{ticket.province}," +
            "city = #{ticket.city}," +
            "area = #{ticket.area}," +
            "open_code = #{ticket.openCode}" +
            "where id = #{ticket.id}")
    int update(@Param("ticket") ParkingTicket ticket);
}
