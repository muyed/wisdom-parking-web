package com.muye.wp.common.cons;

/**
 * Created by muye on 18/1/25.
 */
public enum RespStatus {

    OK(200, "OK"),
    SYS_ERR(-1, "系统异常"),
    NOT_LOGIN(1, "您还没有登录,请先登录"),
    WRONG_LOGIN(2, "登录失败"),
    SMS_ERR(3, "调用短信接口失败"),
    SMS_OFTEN(4, "您调用短信接口过于频繁,请稍后再试"),
    SMS_CODE_WRONG(5, "短信验证码错误"),
    RESOURCE_EXISTED(6, "资源已存在"),
    RESOURCE_NOT_EXIST(7, "资源不存在"),
    BUSINESS_ERR(8, "业务异常"),

    PAY_GEN_INFO_FAIL(10, "生成支付信息失败"),
    PAY_CALLBACK_FAIL(11, "支付回调失败"),

    AUTH_ERR(20, "权限异常,请联系开发"),
    AUTH_NOT(21, "没有权限"),
    AUTH_IDCARD_ERR(22, "实名认证时发生错误"),
    AUTH_IDCARD_NOT(23, "您没有进行实名认证"),
    AUTH_CARPORT_FAIL(24, "认证车位失败"),

    EMBED_SERVER_ERR(800, "embed服务端发生错误"),
    ;

    private int code;
    private String message;

    RespStatus(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
