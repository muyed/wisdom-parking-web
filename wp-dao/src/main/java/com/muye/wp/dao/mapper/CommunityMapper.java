package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.Community;
import com.muye.wp.dao.domain.query.CommunityQuery;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/2/28.
 */
public interface CommunityMapper {

    @Select("select * from community where id = #{id}")
    Community selectById(@Param("id") Long id);

    @Select("select * from community where id = #{id} for update")
    Community selectByIdForUpdate(@Param("id") Long id);

    @Select("<script>" +
            "select * from community where 1 = 1 " +
            "<if test='query.communityName != null'>and community_name = #{query.communityName}</if>" +
            "<if test='query.type != null'>and type = #{query.type}</if>" +
            "</script>")
    List<Community> selectByCondition(@Param("query") CommunityQuery query, Page page);

    @Insert("insert into community (community_name, type, province, city, area, addr) values (" +
            "#{community.communityName}," +
            "#{community.type}," +
            "#{community.province}," +
            "#{community.city}," +
            "#{community.area}," +
            "#{community.addr})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "community.id", resultType = Long.class, before = false)
    int insert(@Param("community") Community community);

    @Update("update community set " +
            "community_name = #{community.communityName}," +
            "type = #{community.type}," +
            "province = #{community.province}," +
            "city = #{community.city}," +
            "area = #{community.area}," +
            "addr = #{community.addr} " +
            "where id = #{community.id}")
    int update(@Param("community") Community community);

    @Delete("delete from community where id = #{id}")
    int delete(@Param("id") Long id);
}
