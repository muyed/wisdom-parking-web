package com.muye.wp.service;

import com.muye.wp.dao.domain.User;

/**
 * Created by muye on 18/1/25.
 */
public interface UserService {

    User queryById(Long id);

    User queryByIdForUpdate(Long id);

    User selectByUsernameAndType(String username, int type);

    User selectByPhoneAndType(String phone, int type);

    int insert(User user);

    int update(User user);

    boolean idcardAuth(Long userId, String realName, String idcard);

    boolean isAuth(Long userId);
}
