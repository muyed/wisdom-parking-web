package com.muye.wp.embed.protocol;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by muye on 18/4/12.
 *
 */
public enum ProtoMethod {

    INIT((byte)0),          //客户端首次链接
    REFRESH((byte) 1),      //重置所有数据
    ADD((byte)2),           //添加业主车牌号
    ADD_TEMP((byte)3),      //添加临时车牌号
    DEL((byte)4),           //删除业主车牌号
    DEL_TEMP((byte)5),      //删除临时车牌号
    GET_PHOTO((byte)6);     //查看照片

    private byte method;

    ProtoMethod(byte method){
        this.method = method;
    }

    public byte getMethod() {
        return method;
    }

    public static ProtoMethod ofMethod(byte method){
        Optional<ProtoMethod> optional = Stream.of(ProtoMethod.values())
                .filter(protoMethod -> protoMethod.method == method)
                .findFirst();
        if (optional.isPresent()) return optional.get();
        return null;
    }
}
