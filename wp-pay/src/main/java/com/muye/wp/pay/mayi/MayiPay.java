package com.muye.wp.pay.mayi;

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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by muye on 18/2/2.
 */
@Component
public class MayiPay {

    private static final Logger logger = LoggerFactory.getLogger(MayiPay.class);

    @Value("${mayi.app_id}")
    private String appId;

    @Value("${mayi.private_key}")
    private String privateKey;

    @Value("${mayi.public_key}")
    private String publicKey;

    @Autowired
    private CapitalFlowService capitalFlowService;

    /**
     * 根据支付流水单 创建支付宝支付单
     */
    public String genPayInfo(String orderNum){
        CapitalFlow capitalFlow = capitalFlowService.queryByOrderNum(orderNum);

        if (capitalFlow == null) throw new WPException(RespStatus.RESOURCE_NOT_EXIST);

        if (!capitalFlow.getStatus().equals(CapitalFlowStatus.ING.getStatus()))
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL, "订单已过期");

        Integer timeoutExpress = capitalFlowService.getTimeoutExpress(capitalFlow);

        return SignUtil.sign(appId, privateKey, capitalFlow, timeoutExpress);
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
            logger.error("参数检验失败: {}", e);
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
