package com.muye.wp.pay.mayi;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.codec.Base64;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.CapitalFlow;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.alipay.api.internal.util.AlipaySignature.getPrivateKeyFromPKCS8;

/**
 * Created by muye on 18/4/1.
 */
public class SignUtil {

    private static final String method = "method=alipay.trade.app.pay";

    private static final String signType = "sign_type=RSA2";

    private static final String charset = "charset=utf-8";

    private static final String notifyUrl = "notify_url=https://api.jsppi.com/api/pay/mayi/callback";

    private static final String version = "version=1.0";

    public static String sign(String appId, String privateKey, CapitalFlow flow, Integer timeoutExpress){

        String sign;
        appId = "app_id=" + appId;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "timestamp=" + sdf.format(new Date());

        DecimalFormat df = new DecimalFormat("0.00");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("body", ProductType.ofType(flow.getType()).getName());
        jsonObject.put("subject", ProductType.ofType(flow.getType()).getName());
        jsonObject.put("out_trade_no", flow.getOrderNum());
        jsonObject.put("timeout_express", timeoutExpress + "m");
        jsonObject.put("total_amount", df.format(flow.getAmount()));
        jsonObject.put("product_code", "QUICK_MSECURITY_PAY");
        String bizContent = "biz_content=" + jsonObject.toJSONString();

        List<String> param = new ArrayList<>();
        param.add(appId);
        param.add(method);
        param.add(charset);
        param.add(timestamp);
        param.add(notifyUrl);
        param.add(version);
        param.add(signType);

        param.add(bizContent);

        Collections.sort(param);

        sign = param.stream().reduce((a, b) -> a + "&" + b).get();

        PrivateKey priKey;
        Signature signature;
        try{
            priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(priKey);
            signature.update(sign.getBytes("utf-8"));
            byte[] signed = signature.sign();
            sign = "sign=" + new String(Base64.encodeBase64(signed));
        }catch (Exception e){
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL);
        }

        param.add(sign);
        return param.stream().map(p -> {
            try {
                p = p.split("=", 2)[0] + "=" + URLEncoder.encode(p.split("=", 2)[1], "utf-8");
            }catch (Exception e){
            }
            return p;
        }).reduce((a, b) -> a + "&" + b).get();
    }
}
