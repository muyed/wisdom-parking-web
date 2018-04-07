package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.ParkingShare;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 18/4/3.
 */
public interface ParkingShareMapper {

    @Select("<script>" +
            "select * from parking_share where 1 =1 " +
            "<if test='query.shareNum != null'>and share_num = #{query.shareNum}</if>" +
            "<if test='query.userId != null'>and user_id = #{query.userId}</if>" +
            "<if test='query.carportId != null'>and carport_id = #{query.carportId}</if>" +
            "<if test='query.startTime != null'>and start_time = #{query.startTime}</if>" +
            "<if test='query.stopTime != null'>and stop_time = #{query.stopTime}</if>" +
            "<if test='query.price != null'>and price = #{query.price}</if>" +
            "<if test='query.status != null'>and status = #{query.status}</if>" +
            "<if test='query.carportMeid != null'>and carport_meid = #{query.carportMeid}</if>" +
            "<if test='query.carportNum != null'>and carport_num = #{query.carportNum}</if>" +
            "<if test='query.communityId != null'>and community_id = #{query.communityId}</if>" +
            "<if test='query.province != null'>and province = #{query.province}</if>" +
            "<if test='query.city != null'>and city = #{query.city}</if>" +
            "<if test='query.area != null'>and area = #{query.area}</if>" +
            "<if test='query.longitude != null'>and longitude = #{query.longitude}</if>" +
            "<if test='query.latitude != null'>and latitude = #{query.latitude}</if>" +
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
    List<ParkingShare> selectListByCondition(@Param("query") ParkingShare query, Page page);

    @Insert("insert into parking_share (share_num, user_id, carport_id, start_time, stop_time, price, status, " +
            "carport_meid, carport_Num,community_id, province, city, area, longitude, latitude) values (" +
            "#{share.shareNum}," +
            "#{share.userId}," +
            "#{share.carportId}," +
            "#{share.startTime}," +
            "#{share.stopTime}," +
            "#{share.price}," +
            "#{share.status}," +
            "#{share.carportMeid}," +
            "#{share.carportNum}," +
            "#{share.communityId}," +
            "#{share.province}," +
            "#{share.city}," +
            "#{share.area}," +
            "#{share.longitude}," +
            "#{share.latitude})")
    int insert(@Param("share") ParkingShare share);
}
