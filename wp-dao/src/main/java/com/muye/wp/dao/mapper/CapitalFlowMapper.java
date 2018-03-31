package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.CapitalFlow;
import org.apache.ibatis.annotations.*;

/**
 * Created by muye on 18/3/30.
 */
public interface CapitalFlowMapper {

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
