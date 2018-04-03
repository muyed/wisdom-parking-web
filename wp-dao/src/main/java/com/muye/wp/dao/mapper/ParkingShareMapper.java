package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.ParkingShare;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * Created by muye on 18/4/3.
 */
public interface ParkingShareMapper {

    @Insert("insert into parking_share (share_num,user_id,carport_id,start_time,stop_time,price,status,carport_meid,carport_num," +
            "community_id,province,city,area,longitude,latitude) values (" +
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
    int insert(@Param("share")ParkingShare share);
}
