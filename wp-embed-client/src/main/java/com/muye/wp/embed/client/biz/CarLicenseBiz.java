package com.muye.wp.embed.client.biz;

import com.muye.wp.embed.mode.TempCarLicense;

import java.util.*;

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

        echo();
        return true;
    }

    public boolean addCarLicense(List<String> carLicenseList){
        boolean result = this.carLicenseList.addAll(carLicenseList);
        echo();
        return result;
    }

    public boolean addTemp(TempCarLicense temp){

        boolean result = tempLicenseList.add(temp);
        echo();
        return result;
    }

    public boolean delCarLicense(List<String> carLicenseList){

        carLicenseList.forEach(this.carLicenseList::remove);
        echo();
        return true;
    }

    public boolean delTemp(Long tempId){
        tempLicenseList.stream()
                .filter(temp -> temp.getId().equals(tempId))
                .findFirst()
                .ifPresent(tempLicenseList::remove);

        echo();
        return true;
    }

    private void echo() {
        System.out.println("当前数据: 业主车牌 ->" + String.join(",", this.carLicenseList));
        System.out.println(this.tempLicenseList.stream().map(t -> t.toString()).reduce("当前数据: 临时车牌 ->", (a, b) -> a + "\n" + b));
    }
}
