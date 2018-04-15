package com.muye.wp.embed.client.biz;

import com.muye.wp.embed.mode.TempCarLicense;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by muye on 18/4/13.
 */
public class CarLicenseBiz {

    private final HashSet<String> carLicenseList = new HashSet<>();   //业主车牌
    private final ArrayList<TempCarLicense> tempLicenseList = new ArrayList<>(); //临时车牌

    public boolean refresh(List<String> carLicenseList, List<TempCarLicense> tempList){
        this.carLicenseList.clear();
        this.tempLicenseList.clear();

        this.carLicenseList.addAll(carLicenseList);
        this.tempLicenseList.addAll(tempList);
        return true;
    }

    public boolean addCarLicense(String carLicense){
        return carLicenseList.add(carLicense);
    }

    public boolean addTemp(TempCarLicense temp){
        return tempLicenseList.add(temp);
    }

    public boolean delCarLicense(String carLicense){
        return carLicenseList.remove(carLicense);
    }

    public boolean delTemp(Long tempId){
        tempLicenseList.stream()
                .filter(temp -> temp.getId().equals(tempId))
                .findFirst()
                .ifPresent(tempLicenseList::remove);
        return true;
    }
}
