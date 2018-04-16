package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.CarLicense;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 18/4/2.
 */
public interface CarLicenseMapper {

    @Select("select * from car_license where id = #{id}")
    CarLicense selectById(@Param("id") Long id);

    @Select("select * from car_license where user_id = #{userId} and license = #{license}")
    CarLicense selectByUserIdAndLicense(@Param("userId") Long userId, @Param("license") String license);

    @Select("select * from car_license where user_id = #{userId}")
    List<CarLicense> selectListByUserId(@Param("userId") Long userId);

    @Insert("insert into car_license (user_id, license) values (#{carLicense.userId}, #{carLicense.license})")
    int insert(@Param("carLicense") CarLicense carLicense);

    @Delete("delete from car_license where id = #{id} and user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);

    //查找某个小区的所有车牌
    @Select("select * from car_license where user_id in (" +
            "   select user_id from user_community where community_id = #{communityId} and type = 2" +
            ")")
    List<CarLicense> selectListByCommunityId(@Param("communityId") Long communityId);
}
