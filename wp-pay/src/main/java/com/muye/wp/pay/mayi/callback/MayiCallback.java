package com.muye.wp.pay.mayi.callback;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by muye on 18/3/31.
 */
public interface MayiCallback {

    void finishedCallback(String orderNum);

    void successCallback(String orderNum);

    void closeCallback(String orderNum);

    static Map<String, String> checkNotify(HttpServletRequest request, String publicKey) throws AlipayApiException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        requestParams.keySet().forEach(name -> params.put(name, String.join(",", requestParams.get(name))));
        AlipaySignature.rsaCheckV1(params, publicKey, "utf-8","RSA2");
        return params;
    }

    static MayiCallback of(ProductType type){
        Class<MayiCallback> clazz = MayiCallback.class;
        switch (type){
            case CARPORT_DEPOSIT: return SpringUtil.getBean("carportCallback", clazz);
            default: throw new WPException(RespStatus.PAY_CALLBACK_FAIL, "没有对应找到回调器");
        }
    }
}
