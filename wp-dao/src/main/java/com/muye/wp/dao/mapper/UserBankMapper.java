package com.muye.wp.dao.mapper;

import com.muye.wp.dao.domain.UserBank;
import com.muye.wp.dao.page.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 18/4/23.
 */
public interface UserBankMapper {

    @Select("<script>" +
            "select * from user_bank where 1 = 1" +
            "<if test='query.userId != null'> and user_id = #{query.userId}</if>" +
            "<if test='query.bankName != null'> and bank_name = #{query.bankName}</if>" +
            "<if test='query.bankAccount != null'> and bank_account = #{query.bankAccount}</if>" +
            "<if test='query.accountName != null'> and account_name = #{query.accountName}</if>" +
            "<if test='query.bankAddr != null'> and bank_addr = #{query.bankAddr}</if>" +
            "<if test='query.bankCode != null'> and bank_code = #{query.bankCode}</if>" +
            "</script>")
    List<UserBank> selectListByCondition(@Param("query") UserBank query, Page page);

    @Select("select * from user_bank where id = #{id}")
    UserBank selectById(@Param("id") Long id);

    @Insert("insert into user_bank (" +
            "user_id, bank_name, bank_account, account_name, bank_addr, bank_code" +
            ") values (" +
            "#{userBank.userId}," +
            "#{userBank.bankName}," +
            "#{userBank.bankAccount}," +
            "#{userBank.accountName}," +
            "#{userBank.bankAddr}," +
            "#{userBank.bankCode})")
    int insert(@Param("userBank")UserBank userBank);
}
