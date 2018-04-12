package com.muye.wp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.Account;
import com.muye.wp.dao.domain.User;
import com.muye.wp.dao.mapper.UserMapper;
import com.muye.wp.service.AccountService;
import com.muye.wp.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

/**
 * Created by muye on 18/1/25.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private AccountService accountService;

    @Value("${idcard.app_code}")
    private String appCode;

    @Value("${idcard.host}")
    private String idcardHost;

    @Value("${idcard.path}")
    private String idcardPath;

    @Override
    public User queryById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User queryByIdForUpdate(Long id) {
        return userMapper.selectByIdForUpdate(id);
    }

    @Override
    public User selectByUsernameAndType(String username, int type) {
        return userMapper.selectByUsernameAndType(username, type);
    }

    @Override
    public User selectByPhoneAndType(String phone, int type) {
        return userMapper.selectByPhoneAndType(phone, type);
    }

    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    @Transactional
    public void reg(User user) {
        insert(user);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setBalance(BigDecimal.ZERO);
        account.setCash(BigDecimal.ZERO);
        accountService.add(account);
    }

    @Override
    @Transactional
    public boolean idcardAuth(Long userId, String realName, String idcard) {

        User user = queryByIdForUpdate(userId);
        if (StringUtils.isNotEmpty(user.getIdentityCard()))
            throw new WPException(RespStatus.BUSINESS_ERR, "已经实名认证过了");

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(idcardHost)
                    .setPath(idcardPath)
                    .setParameter("name", realName)
                    .setParameter("idcard", idcard)
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader("Authorization", "APPCODE " + appCode);
            httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            response.close();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if ((Integer)jsonObject.get("showapi_res_code") == 0 &&
                    (Integer)((Map)jsonObject.get("showapi_res_body")).get("code") == 0){

                user.setIdentityCard(idcard);
                user.setRealName(realName);
                userMapper.update(user);
                return true;
            }
            throw new WPException(RespStatus.AUTH_IDCARD_FAIL);
        }catch (Exception e){
            throw new WPException(RespStatus.AUTH_IDCARD_ERR, e);
        }
    }

    @Override
    public boolean isAuth(Long userId) {
        User user = queryById(userId);
        if (user != null && StringUtils.isNotEmpty(user.getIdentityCard())){
            return true;
        }
        return false;
    }
}
