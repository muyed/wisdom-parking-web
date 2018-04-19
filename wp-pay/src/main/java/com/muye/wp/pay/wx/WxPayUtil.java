package com.muye.wp.pay.wx;

import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.CapitalFlow;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by muye on 18/4/19.
 */
public class WxPayUtil {

    private static final String appId = "wx39fab7de09774fbd";
    private static final String mchId = "1502374131";
    private static final String notifyUrl = "https://api.jsppi.com/api/pay/wx/callback";
    private static final String spbillCreateIp = "127.0.0.1";
    private static final String tradeType = "APP";
    private static final String key = "487736af09f24629830f9cc7dd38127e";
    private static final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static String payInfo(CapitalFlow flow, LocalDateTime endTime){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        String body = ProductType.ofType(flow.getType()).getCode();
        int totalFee = flow.getAmount().multiply(new BigDecimal("100")).intValue();
        String timeStart = LocalDateTime.now().format(dtf);
        String timeEnd = endTime.format(dtf);

        List<String> params = new ArrayList();
        params.add("appid=" + appId);
        params.add("mch_id=" + mchId);
        params.add("nonce_str=" + nonceStr);
        params.add("body=" + body);
        params.add("out_trade_no=" + flow.getOrderNum());
        params.add("total_fee=" + totalFee);
        params.add("spbill_create_ip=" + spbillCreateIp);
        params.add("time_start=" + timeStart);
        params.add("time_expire=" + timeEnd);
        params.add("notify_url=" + notifyUrl);
        params.add("trade_type=" + tradeType);

        Collections.sort(params);

        String sign = params.stream().reduce((a, b) -> a + "&" + b).get() + "&key=" + key;
        sign = DigestUtils.md5Hex(sign).toUpperCase();

        params.add("sign=" + sign);

        String xml = params.stream()
                .map(param -> {
                    String[] s = param.split("=", 2);
                    StringBuffer sb = new StringBuffer();
                    sb.append("<" + s[0] + ">");
                    sb.append(s[1]);
                    sb.append("</" + s[0] + ">");
                    return sb.toString();
                })
                .reduce("<xml>", (a, b) -> a + b);

        xml += "</xml>";

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type","text/plain");

            StringEntity entity = new StringEntity(xml, Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("text/plain");
            post.setEntity(entity);

            HttpResponse response = httpClient.execute(post);
            String result = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));

            return result;
        }catch (Exception e){
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL);
        }
    }

    public static Map<String, String> xmlToMap(String xml) throws DocumentException{
        Map<String, String> map = new HashMap<>();
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        Iterator<Element> iterator = root.elementIterator();
        while (iterator.hasNext()){
            Element element = iterator.next();
            if (StringUtils.isNotEmpty(element.getTextTrim())) map.put(element.getName(), element.getTextTrim());
        }
        return map;
    }

    public static String callbackSign(Map<String, String> params){

        List<String> list = new ArrayList<>();
        params.keySet()
                .stream()
                .filter(key -> !key.equals("return_code") && !key.equals("return_msg") && !key.equals("result_code"))
                .map(key -> key + "=" + params.get(key))
                .forEach(list::add);

        Collections.sort(list);

        String sign = list.stream().reduce((a, b) -> a + "&" + b).get() + "&key=" + key;
        return DigestUtils.md5Hex(sign).toUpperCase();
    }
}
