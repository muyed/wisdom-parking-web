package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by muye on 18/1/25.
 */
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User selectById(@Param("id") Long id);

    @Select("select * from user where id = #{id} for update")
    User selectByIdForUpdate(@Param("id") Long id);

    @Select("select * from user where username = #{username} and type = #{type}")
    User selectByUsernameAndType(@Param("username") String username, @Param("type") int type);

    @Select("select * from user where phone = #{phone} and type = #{type}")
    User selectByPhoneAndType(@Param("phone") String phone, @Param("type") int type);

    @Insert("insert into user (username, password, phone, type, real_name, identity_card) values (" +
            "#{user.username}," +
            "#{user.password}," +
            "#{user.phone}," +
            "#{user.type}," +
            "#{user.realName}," +
            "#{user.identityCard})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "user.id", resultType = Long.class, before = false)
    int insert(@Param("user") User user);

    @Update("update user set " +
            "username = #{user.username}," +
            "password = #{user.password}," +
            "phone = #{user.phone}," +
            "type = #{user.type}," +
            "real_name = #{user.realName}, " +
            "identity_card = #{user.identityCard} " +
            "where id = #{user.id}")
    int update(@Param("user") User user);
}
