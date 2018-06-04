package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.Notice;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 18/6/4.
 */
public interface NoticeMapper {

    @Select("<script>" +
            "select * from notice where 1 = 1 " +
            "<if test='query.title != null'>and title = #{query.title}</if>" +
            "<if test='query.body != null'>and body = #{query.body}</if>" +
            "<if test='query.status != null'>and status = #{query.status}</if>" +
            "</script>")
    List<Notice> selectListByCondition(@Param("query") Notice query, Page page);

    @Insert("insert into notice (title, body, status) values (#{notice.title}, #{notice.body}, #{notice.status})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "notice.id", resultType = Long.class, before = false)
    int insert(@Param("notice") Notice notice);

    @Update("update notice set " +
            "title = #{notice.title}," +
            "body = #{notice.body}, " +
            "status = #{notice.status} " +
            "where id = #{notice.id}")
    int update(@Param("notice") Notice notice);
}
