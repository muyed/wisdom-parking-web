package com.muye.wp.pay.wx;

import com.muye.wp.common.cons.Bank;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.CapitalFlow;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.KeyStore;
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

    private static final String payBankUrl = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";
    private static final String wxPriKeyPath = "/usr/local/wp/wp-wap/config/apiclient_cert.p12";
    private static final String wxPubKeyPath = "/usr/local/wp/wp-wap/config/wx_pub.pem";

    public static Map<String, String> payInfo(CapitalFlow flow, LocalDateTime endTime){

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

        String xml = listToXml(params);
        try {
            Map<String, String> map = WxPayUtil.xmlToMap(requestWx(url, xml, false));
            map.put("partnerid", map.remove("mch_id"));
            map.put("prepayid", map.remove("prepay_id"));
            map.put("package", "Sign=WXPay");
            map.put("noncestr", UUID.randomUUID().toString().replace("-", ""));
            map.put("timestamp", new Date().getTime() + "");

            map.remove("sign");
            map.remove("trade_type");
            map.remove("return_msg");
            map.remove("result_code");
            map.remove("return_code");

            List<String> payParams = new ArrayList<>();

            map.keySet()
                    .stream()
                    .map(key -> key +"=" + map.get(key))
                    .forEach(payParams::add);

            Collections.sort(payParams);

            String paySign = payParams.stream().reduce((a, b) -> a + "&" + b).get() + "&key=" + key;
            paySign = DigestUtils.md5Hex(paySign).toUpperCase();
            map.put("sign", sign);
            return map;
        }catch (Exception e){
            throw new WPException(RespStatus.PAY_GEN_INFO_FAIL);
        }
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

    public static void withdraw(String encBankNo, String encTrueName, int bankCode, BigDecimal amount, String orderNum, String desc){

        if (Bank.ofBankCode(bankCode) == null) throw new WPException(RespStatus.WITHDRAW_ERR, "暂不支持改银行");
        String nonceStr = UUID.randomUUID().toString().replace("-", "");

        List<String> params = new ArrayList<>();
        params.add("mch_id=" + mchId);
        params.add("partner_trade_no=" + orderNum);
        params.add("nonce_str=" + nonceStr);

        //todo
        params.add("enc_bank_no=" + encBankNo);
        params.add("enc_true_name=" + encTrueName);

        params.add("bank_code=" + bankCode);
        params.add("amount=" + amount.multiply(new BigDecimal("100")).intValue());
        params.add("desc=" + desc);

        Collections.sort(params);

        String sign = params.stream().reduce((a, b) -> a + "&" + b).get() + "&key=" + key;
        sign = DigestUtils.md5Hex(sign).toUpperCase();

        params.add("sign=" + sign);

        String xml = listToXml(params);

        try {
            Map<String, String> resp = xmlToMap(requestWx(payBankUrl, xml, true));
            if (!resp.get("return_code").equals("SUCCESS")) throw new WPException(RespStatus.WITHDRAW_ERR, resp.get("err_code_des"));
        }catch (Exception e){
            throw new WPException(RespStatus.WITHDRAW_ERR, "调用微信接口失败");
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

    public static String listToXml(List<String> params){
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
        return xml;
    }

    public static String requestWx(String url, String xml, boolean ssl) throws Exception{
        HttpClient httpClient;
        if (ssl){
            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
            FileInputStream in = new FileInputStream(new File(wxPriKeyPath));
            keyStore.load(in, mchId.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type","text/plain");
        StringEntity entity = new StringEntity(xml, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("text/plain");
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        return EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
    }



//    public static void main(String[] args) throws Exception{
//
//        List<String> params = new ArrayList<>();
//
//        String nonceStr = UUID.randomUUID().toString().replace("-", "");
//
//        params.add("mch_id=" + mchId);
//        params.add("nonce_str=" + nonceStr);
//        params.add("sign_type=MD5");
//
//        Collections.sort(params);
//
//        String sign = params.stream().reduce((a, b) -> a + "&" + b).get() + "&key=" + key;
//        sign = DigestUtils.md5Hex(sign).toUpperCase();
//
//        params.add("sign=" + sign);
//
//        String xml = listToXml(params);
//
//        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
//        FileInputStream instream = new FileInputStream(new File("/Users/tbj/Downloads/cert/apiclient_cert.p12"));
//        keyStore.load(instream, mchId.toCharArray());
//        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
//        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
//                new String[]{"TLSv1"},
//                null,
//                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//
//        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(factory).build();
//
//        HttpPost post = new HttpPost("https://fraud.mch.weixin.qq.com/risk/getpublickey");
//        post.setHeader("Content-Type","text/plain");
//        StringEntity entity = new StringEntity(xml, Charset.forName("UTF-8"));
//        entity.setContentEncoding("UTF-8");
//        entity.setContentType("text/plain");
//        post.setEntity(entity);
//
//        HttpResponse response = httpclient.execute(post);
//
//        String resp = EntityUtils.toString(response.getEntity());
//
//        Map<String, String> map = xmlToMap(resp);
//
//        System.out.println(map.get("pub_key"));
//
//    }
}
