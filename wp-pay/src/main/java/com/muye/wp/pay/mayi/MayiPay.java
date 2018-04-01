package com.muye.wp.pay.mayi;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.muye.wp.common.cons.CapitalFlowDirection;
import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.pay.mayi.callback.MayiCallback;
import com.muye.wp.service.CapitalFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by muye on 18/2/2.
 */
@Component
public class MayiPay {

    private static final Logger logger = LoggerFactory.getLogger(MayiPay.class);

    private AlipayClient client;

    @Value("${mayi.app_id}")
    private String appId;

    @Value("${mayi.private_key}")
    private String privateKey;

    @Value("${mayi.public_key}")
    private String publicKey;

    @Value("${mayi.gateway}")
    private String gateway;

    @Value("${mayi.sign}")
    private String signType;

    @Value("${mayi.format}")
    private String format;

    @Autowired
    private CapitalFlowService capitalFlowService;

    private String charset = "utf-8";
    private static final String callback = "https://api.jsppi.com/api/pay/mayi/callback";

    @PostConstruct
    public void init(){
        client = new DefaultAlipayClient(gateway, appId, privateKey, format, charset, publicKey, signType);
    }

    /**
     * 根据支付流水单 创建支付宝支付单
     */
    public String genPayInfo(String orderNum){

        CapitalFlow capitalFlow = capitalFlowService.queryByOrderNum(orderNum);
        if (capitalFlow == null) {
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        }
        if (capitalFlow.getDirection() != CapitalFlowDirection.OUT.getDirection()){
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL, "流水单类型不是支出型");
        }
        if (capitalFlow.getStatus() != CapitalFlowStatus.ING.getStatus()){
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL, "订单已过期");
        }

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(ProductType.ofType(capitalFlow.getType()).getName());
        model.setOutTradeNo(orderNum);
        model.setSubject(ProductType.ofType(capitalFlow.getType()).getName());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(capitalFlow.getAmount().toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(MayiPay.callback);

        try {
            AlipayTradeAppPayResponse response = client.sdkExecute(request);
            String body = response.getBody().replaceFirst("alipay_sdk=alipay-sdk-java-dynamicVersionNo&", "");
            logger.info("生成支付信息成功: orderNum: {}, body: {}", orderNum, body);
            return body;
        }catch (AlipayApiException e){
            logger.error("生成支付信息失败: orderNum: {}，e: {}", orderNum, e);
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL, e);
        }
    }

    /**
     * 支付宝回调
     */
    public void callback(HttpServletRequest request){
        logger.info("支付宝回调中...");
        Map<String, String> params;
        try {
            params =  MayiCallback.checkNotify(request, publicKey);
        } catch (Exception e){
            throw new WPException(RespStatus.PAY_CALLBACK_FAIL, "参数检验失败");
        }

        String productName = params.get("subject");
        String orderNum = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");

        ProductType type = ProductType.ofName(productName);

        MayiCallback callback = MayiCallback.of(type);

        switch (tradeStatus){
            case "TRADE_FINISHED":
                callback.finishedCallback(orderNum);
                break;
            case "TRADE_SUCCESS":
                callback.successCallback(orderNum);
                break;
            case "TRADE_CLOSED":
                callback.closeCallback(orderNum);
                break;
            default:
                logger.warn("回调类型：{}， PASS", tradeStatus);
        }
    }
}
