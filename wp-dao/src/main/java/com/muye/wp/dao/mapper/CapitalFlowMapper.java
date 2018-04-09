package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/3/30.
 */
public interface CapitalFlowMapper {

    @Select("<script>" +
            "select * from capital_flow where 1 = 1 " +
            "<if test='flow.userId != null'>and user_id = #{flow.userId}</if>" +
            "<if test='flow.direction != null'>and direction = #{flow.direction}</if>" +
            "<if test='flow.type != null'>and type = #{flow.type}</if>" +
            "<if test='flow.orderNum != null'>and order_num = #{flow.orderNum}</if>" +
            "<if test='flow.amount != null'>and amount = #{flow.amount}</if>" +
            "<if test='flow.status != null'>and status = #{flow.status}</if>" +
            "</script>")
    List<CapitalFlow> selectListByCondition(@Param("flow") CapitalFlow flow, Page page);

    @Insert("insert into capital_flow (user_id, direction, type, order_num, amount, status) values (" +
            "#{flow.userId}," +
            "#{flow.direction}," +
            "#{flow.type}," +
            "#{flow.orderNum}," +
            "#{flow.amount}," +
            "#{flow.status}" +
            ")")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "flow.id", resultType = Long.class, before = false)
    int insert(@Param("flow") CapitalFlow flow);

    @Select("select * from capital_flow where order_num = #{orderNum}")
    CapitalFlow selectByOrderNum(@Param("orderNum") String orderNum);

    @Update("update capital_flow set " +
            "user_id = #{flow.userId}," +
            "direction = #{flow.direction}," +
            "type = #{flow.type}," +
            "order_num = #{flow.orderNum}," +
            "amount = #{flow.amount}," +
            "status = #{flow.status} " +
            "where id = #{flow.id}" +
            "")
    int update(@Param("flow") CapitalFlow flow);
}
