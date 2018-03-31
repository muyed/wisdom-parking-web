package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.query.CarportQuery;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/3/6.
 */
public interface CarportMapper {

    @Select("select * from carport where id = #{id}")
    Carport selectById(@Param("id") Long id);

    @Select("select * from carport where id = #{id} for update")
    Carport selectByIdForUpdate(@Param("id") Long id);

    @Select("<script>" +
            "select * from carport where 1 = 1" +
            "<if test='query.communityModuleId'>and community_module_id = #{query.communityModuleId}</if>" +
            "<if test='query.communityId'>and community_id = #{query.communityId}</if>" +
            "<if test='query.meid'>and meid = #{query.meid}</if>" +
            "<if test='query.shareStatus'>and shareStatus = #{query.shareStatus}</if>" +
            "<if test='query.lockStatus'>and lockStatus = #{query.lockStatus}</if>" +
            "<if test='query.sortList != null'>" +
            "   order by" +
            "   <foreach collection='query.sortList' item='item' separator=','>${item.column} ${item.sort}</foreach>" +
            "</if>" +
            "</script>")
    List<Carport> selectListByCondition(@Param("query") CarportQuery query, Page page);

    @Insert("insert into carport (community_module_id, community_id, meid, bind_code, longitude, latitude, share_status, lock_status) values (" +
            "#{carport.communityModuleId}," +
            "#{carport.communityId}," +
            "#{carport.meid}," +
            "#{carport.bindCode}," +
            "#{carport.longitude}," +
            "#{carport.latitude}," +
            "#{carport.shareStatus}," +
            "#{carport.lockStatus})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "carport.id", resultType = Long.class, before = false)
    int insert(@Param("carport") Carport carport);

    @Update("update carport set " +
            "community_module_id = #{carport.communityModuleId}," +
            "community_id = #{carport.communityId}," +
            "meid = #{carport.meid}," +
            "bind_code = #{carport.bindCode}," +
            "longitude = #{carport.longitude}," +
            "latitude = #{carport.latitude}," +
            "share_status = #{carport.shareStatus}," +
            "lock_status = #{carport.lockStatus} " +
            "where id = #{carport.id}")
    int update(@Param("carport") Carport carport);

    @Delete("delete from carport where id = #{id}")
    int delete(@Param("id") Long id);
}
