package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.CommunityModule;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/3/1.
 */
public interface CommunityModuleMapper {

    @Select("select * from community_module where id = #{id} for update")
    CommunityModule selectByIdForUpdate(@Param("id") Long id);

    @Select("select * from community_module where id = #{id}")
    CommunityModule selectById(@Param("id") Long id);

    @Select("<script>" +
            "select * from community_module where 1 = 1" +
            "<if test='query.communityId != null'>and community_id = #{query.communityId}</if>" +
            "<if test='query.moduleName != null'>and module_name = #{query.moduleName}</if>" +
            "</script>")
    List<CommunityModule> selectByCondition(@Param("query") CommunityModule query, Page page);

    @Insert("insert into community_module (community_id, module_name, longitude, latitude) values (" +
            "#{communityModule.communityId}," +
            "#{communityModule.moduleName}," +
            "#{communityModule.longitude}," +
            "#{communityModule.latitude})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "communityModule.id", resultType = Long.class, before = false)
    int insert(@Param("communityModule") CommunityModule communityModule);

    @Update("update community_module set " +
            "community_id = #{communityModule.communityId}," +
            "module_name = #{communityModule.moduleName}," +
            "longitude = #{communityModule.longitude}," +
            "latitude = #{communityModule.latitude}" +
            "where id = #{communityModule.id}")
    int update(@Param("communityModule") CommunityModule communityModule);

    @Delete("delete from community_module where id = #{id}")
    int delete(@Param("id") Long id);
}
