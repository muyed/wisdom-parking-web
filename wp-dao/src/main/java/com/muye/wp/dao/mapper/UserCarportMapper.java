package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/3/15.
 */
public interface UserCarportMapper {

    @Select("<script>" +
            "select * from user_carport where 1 = 1" +
            "<if test='query.userId != null'>and user_id = #{query.userId}</if>" +
            "<if test='query.carportId != null'>and carport_id = #{query.carportId}</if>" +
            "<if test='query.payNum != null'>and pay_num = #{query.payNum}</if>" +
            "<if test='query.status != null'>and status = #{query.status}</if>" +
            "<if test='query.parent != null'>and parent = #{query.parent}</if>" +
            "</script>")
    List<UserCarport> selectByCondition(@Param("query") UserCarport query, Page page);

    @Select("select * from user_carport where id = #{id}")
    UserCarport selectById(@Param("id") Long id);

    @Select("select * from user_carport where pay_num = #{payNum}")
    UserCarport queryByPayNum(@Param("payNum") String payNum);

    @Insert("insert into user_carport (user_id, carport_id, alias, pay_num, deposit, status, parent) values (" +
            "#{userCarport.userId}," +
            "#{userCarport.carportId}," +
            "#{userCarport.alias}," +
            "#{userCarport.payNum}," +
            "#{userCarport.deposit}," +
            "#{userCarport.status}," +
            "#{userCarport.parent}" +
            ")")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "userCarport.id", resultType = Long.class, before = false)
    int insert(@Param("userCarport") UserCarport userCarport);

    @Update("update user_carport set " +
            "user_id = #{userCarport.userId}," +
            "carport_id = #{userCarport.carportId}," +
            "alias = #{userCarport.alias}," +
            "pay_num = #{userCarport.payNum}," +
            "deposit = #{userCarport.deposit}," +
            "status = #{userCarport.status}," +
            "parent = #{userCarport.parent} " +
            "where id = #{userCarport.id}")
    int update(@Param("userCarport") UserCarport userCarport);
}
