package com.muye.wp.embed.server.door.service;

import com.muye.wp.embed.mode.TempCarLicense;

import java.util.List;

/**
 * Created by muye on 18/4/12.
 */
public interface DoorEmbedService {

    /**
     * 刷新小区嵌入式设备内车牌数据
     */
    boolean refreshCommunityDate(Long communityId);

    /**
     * 添加业主车牌号到小区嵌入式设备内
     */
    boolean addCarLicense(Long communityId, List<String> carLicenseList);

    /**
     * 添加临时车牌号到小区嵌入式设备内
     */
    boolean addTempLicense(Long communityId, TempCarLicense temp);

    /**
     * 删除业主车牌
     */
    boolean delCarLicense(Long communityId, List<String> carLicenseList);

    /**
     * 删除临时车牌
     */
    boolean delTempLicense(Long communityId, Long tempId);

    /**
     * 获取车辆进场/离场图片
     */
    byte[] getPhoto(Long communityId, String fileDirAndName);

}
