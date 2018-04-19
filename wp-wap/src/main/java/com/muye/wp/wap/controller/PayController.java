package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.pay.mayi.MayiPay;
import com.muye.wp.pay.wx.WxPay;
import com.muye.wp.pay.wx.WxPayUtil;
import com.muye.wp.wap.security.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by muye on 18/2/15.
 */
@RestController
@Auth
@RequestMapping("/api/pay")
public class PayController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private MayiPay mayiPay;

    @Autowired
    private WxPay wxPay;

    @Auth(UserType.GENERAL)
    @GetMapping(value = "/mayi/{orderNum}")
    public Result<String> mayi(@PathVariable String orderNum){

        String payInfo = mayiPay.genPayInfo(orderNum);
        return Result.ok(payInfo);
    }

    @RequestMapping(value = "/mayi/callback")
    @ResponseBody
    public String mayiCallback(HttpServletRequest request){

        mayiPay.callback(request);
        return "success";
    }

    @Auth(UserType.GENERAL)
    @GetMapping("/wx/{orderNum}")
    public Result<String> wx(@PathVariable String orderNum){

        return Result.ok(wxPay.genPayInfo(orderNum));
    }

    @RequestMapping(value = "/wx/callback")
    @ResponseBody
    public String wxCallback(HttpServletRequest request){

        try {
            InputStream in = request.getInputStream();
            byte[] buffer = new byte[request.getContentLength()];
            in.read(buffer);
            String xml = new String(buffer, "UTF-8");

            Map<String, String> params = WxPayUtil.xmlToMap(xml);

            System.out.println();
        } catch (Exception e){

        }

        return "";
    }

}
