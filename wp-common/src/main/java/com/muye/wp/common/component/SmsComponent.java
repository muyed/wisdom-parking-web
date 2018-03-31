package com.muye.wp.common.component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by muye on 18/1/26.
 */
//@Component
public class SmsComponent {

    private static Logger logger = LoggerFactory.getLogger(SmsComponent.class);

    @Value("${sms.key}")
    private String key;

    @Value("${sms.keySecret}")
    private String keySecret;

    final String product = "Dysmsapi";
    final String domain = "dysmsapi.aliyuncs.com";
    final String regionId = "cn-hangzhou";

    public String send(String phone, String temp, String tempParam){

        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            IClientProfile profile = DefaultProfile.getProfile(regionId, key, keySecret);
            DefaultProfile.addEndpoint(regionId, regionId, product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phone);
            request.setSignName("云泊物联");
            request.setTemplateCode(temp);

            request.setTemplateParam(tempParam);

            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (!"OK".equals(sendSmsResponse.getCode())){
                logger.error("调用短信异常：" + sendSmsResponse.getMessage());
                throw new WPException(RespStatus.SMS_ERR);
            }
            return sendSmsResponse.getCode();
        }catch (ClientException e){
            throw new WPException(RespStatus.SMS_ERR, e);
        }

    }
}
