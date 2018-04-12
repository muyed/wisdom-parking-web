//package com.muye.wp.embed.server.service;
//
//import com.muye.wp.embed.mode.TempCarLicense;
//import com.muye.wp.embed.protocol.PushResult;
//
//import java.util.List;
//
///**
// * Created by muye on 18/4/12.
// */
//public interface EmbedService {
//
//    /**
//     * 刷新小区嵌入式设备内车牌数据
//     */
//    PushResult refreshCommunityDate(Long communityId, List<String> carLicenseList, List<TempCarLicense> tempList);
//
//    /**
//     * 添加业主车牌号到小区嵌入式设备内
//     */
//    PushResult addCarLicense(Long communityId, String carLicense);
//
//    /**
//     * 添加临时车牌号到小区嵌入式设备内
//     */
//    PushResult addTempLicense(Long communityId, TempCarLicense temp);
//
//    /**
//     * 删除业主车牌
//     */
//    PushResult delCarLicense(Long communityId, String carLicense);
//
//    /**
//     * 删除临时车牌
//     */
//    PushResult delTempLicense(Long communityId, Long tempId);
//
//    /**
//     * 获取车辆进场/离场图片
//     */
//    PushResult getPhoto(Long communityId, String fileDirAndName);
//
//}
