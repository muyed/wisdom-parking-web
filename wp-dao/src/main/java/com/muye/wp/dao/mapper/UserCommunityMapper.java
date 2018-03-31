package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.UserCommunity;
import com.muye.wp.dao.domain.query.UserCommunityQuery;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/3/2.
 */
public interface UserCommunityMapper {

    @Select("<script>" +
            "select * from user_community where 1 = 1 " +
            "<if test='query.userId != null'>and user_id = #{query.userId}</if>" +
            "<if test='query.communityId != null'>and community_id = #{query.communityId}</if>" +
            "<if test='query.type != null'>and type = #{query.type}</if>" +
            "</script>")
    List<UserCommunity> selectByCondition(@Param("query") UserCommunityQuery query, Page page);

    @Select("select * from user_community where id = #{id} for update")
    UserCommunity selectByIdForUpdate(@Param("id") Long id);

    @Insert("insert into user_community (user_id, community_id, type, floor_no, unit_no, house_no) values (" +
            "#{userCommunity.userId}," +
            "#{userCommunity.communityId}," +
            "#{userCommunity.type}," +
            "#{userCommunity.floorNo}," +
            "#{userCommunity.unitNo}," +
            "#{userCommunity.houseNo})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "userCommunity.id", resultType = Long.class, before = false)
    int insert(@Param("userCommunity") UserCommunity userCommunity);

    @Update("<script>" +
            "update user_community" +
            "<set>" +
            "<if test='userCommunity.userId != null'>user_id = #{userCommunity.userId},</if>" +
            "<if test='userCommunity.communityId != null'>community_id = #{userCommunity.communityId},</if>" +
            "<if test='userCommunity.type != null'>type = #{userCommunity.type},</if>" +
            "<if test='userCommunity.floorNo != null'>floor_no = #{userCommunity.floorNo},</if>" +
            "<if test='userCommunity.unitNo != null'>unit_no = #{userCommunity.unitNo},</if>" +
            "<if test='userCommunity.houseNo != null'>house_no = #{userCommunity.houseNo},</if>" +
            "</set>" +
            "where id = #{userCommunity.id}" +
            "</script>")
    int updateSelective(@Param("userCommunity") UserCommunity userCommunity);
}
