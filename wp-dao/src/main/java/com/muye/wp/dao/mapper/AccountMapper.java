package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by muye on 18/4/9.
 */
public interface AccountMapper {

    @Select("select * from account where user_id = #{userId} for update")
    Account selectByUserIdForUpdate(@Param("userId") Long userId);

    @Select("select * from account where user_id = #{userId}")
    Account selectByUserId(@Param("userId") Long userId);

    @Insert("insert into account (user_id, balance, cash) values (#{account.userId}, #{account.balance}, #{account.cash})")
    int insert(@Param("account") Account account);

    @Update("update account set " +
            "user_id = #{account.userId}," +
            "balance = #{account.balance}," +
            "cash = #{account.cash} " +
            "where id = #{account.id}")
    int update(@Param("account") Account account);
}
