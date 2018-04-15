package com.muye.wp.embed.client.core;

import com.muye.wp.embed.client.biz.CarLicenseBiz;
import com.muye.wp.embed.mode.TempCarLicense;
import com.muye.wp.embed.protocol.Proto;
import com.muye.wp.embed.protocol.ProtoType;

import java.util.List;

/**
 * Created by muye on 18/4/13.
 */
public class Router {

    private CarLicenseBiz carLicenseBiz = new CarLicenseBiz();

    public Object router(Proto proto){
        if (proto.getType() == ProtoType.ASK){
            switch (proto.getMethod()){
                case REFRESH:
                    return carLicenseBiz.refresh((List<String>) proto.getBody().get(0), (List<TempCarLicense>) proto.getBody().get(1));
                case ADD:
                    return carLicenseBiz.addCarLicense((String) proto.getBody().get(0));
                case ADD_TEMP:
                    return carLicenseBiz.addTemp((TempCarLicense) proto.getBody().get(0));
                case DEL:
                    return carLicenseBiz.delCarLicense((String) proto.getBody().get(0));
                case DEL_TEMP:
                    return carLicenseBiz.delTemp((Long) proto.getBody().get(0));
                default: return "没有指定类型任务";
            }
        }

        return null;
    }
}
