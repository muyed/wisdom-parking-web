package com.muye.wp.embed.server.door.service;

import com.muye.wp.common.cons.ParkingTicketStatus;
import com.muye.wp.common.utils.DateUtil;
import com.muye.wp.dao.domain.CarLicense;
import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.dao.mapper.CarLicenseMapper;
import com.muye.wp.dao.mapper.ParkingTicketMapper;
import com.muye.wp.embed.mode.TempCarLicense;
import com.muye.wp.embed.protocol.Proto;
import com.muye.wp.embed.protocol.ProtoMethod;
import com.muye.wp.embed.protocol.ProtoType;
import com.muye.wp.embed.server.door.core.Invoke;
import com.muye.wp.embed.server.door.core.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/4/12.
 */
@Component
public class DoorEmbedServiceImpl implements DoorEmbedService {

    @Autowired(required = false)
    private CarLicenseMapper carLicenseMapper;

    @Autowired(required = false)
    private ParkingTicketMapper parkingTicketMapper;

    @Override
    public boolean refreshCommunityDate(Long communityId) {

        List<CarLicense> carLicenseList = carLicenseMapper.selectListByCommunityId(communityId);
        List<String> carLicenseNumList = new ArrayList<>(carLicenseList.size());
        carLicenseList.forEach(license -> carLicenseNumList.add(license.getLicense()));

        ParkingTicket query = new ParkingTicket();
        query.setCommunityId(communityId);
        query.setStatus(ParkingTicketStatus.PAID.getStatus());
        List<ParkingTicket> ticketList = parkingTicketMapper.selectListByCondition(query, null);
        List<TempCarLicense> tempList = new ArrayList<>(ticketList.size());
        ticketList.forEach(ticket -> {
            TempCarLicense temp = new TempCarLicense();
            temp.setCarLicense(ticket.getCarLicense());
            temp.setId(ticket.getId());
            tempList.add(temp);
        });

        Proto proto = new Proto().putType(ProtoType.ASK)
                .putMethod(ProtoMethod.REFRESH)
                .addBodyItem(carLicenseNumList)
                .addBodyItem(tempList);

        Invoke invoke = new Invoke(proto, Server.getClient(communityId).getSocketList());
        try {
            List<Object> list = invoke.invoke();
            return list.stream().anyMatch(resp -> (Boolean)resp);
        }catch (Exception e){
        }

        return false;
    }

    @Override
    public boolean addCarLicense(Long communityId, List<String> carLicenseList) {

        Proto proto = new Proto().putType(ProtoType.ASK)
                .putMethod(ProtoMethod.ADD)
                .addBodyItem(carLicenseList);

        try {
            Invoke invoke = new Invoke(proto, Server.getClient(communityId).getSocketList());
            List<Object> list = invoke.invoke();
            return list.stream().anyMatch(resp -> (Boolean)resp);
        }catch (Exception e){
        }

        return false;
    }

    @Override
    public boolean addTempLicense(Long communityId, TempCarLicense temp) {

        Proto proto = new Proto().putType(ProtoType.ASK)
                .putMethod(ProtoMethod.ADD_TEMP)
                .addBodyItem(temp);

        try {
            Invoke invoke = new Invoke(proto, Server.getClient(communityId).getSocketList());
            List<Object> list = invoke.invoke();
            return list.stream().anyMatch(resp -> (Boolean)resp);
        }catch (Exception e){
        }

        return false;
    }

    @Override
    public boolean delCarLicense(Long communityId, List<String> carLicenseList) {

        Proto proto = new Proto().putType(ProtoType.ASK)
                .putMethod(ProtoMethod.DEL)
                .addBodyItem(carLicenseList);

        try {
            Invoke invoke = new Invoke(proto, Server.getClient(communityId).getSocketList());
            List<Object> list = invoke.invoke();
            return list.stream().anyMatch(resp -> (Boolean)resp);
        }catch (Exception e){
        }

        return false;
    }

    @Override
    public boolean delTempLicense(Long communityId, Long tempId) {

        Proto proto = new Proto().putType(ProtoType.ASK)
                .putMethod(ProtoMethod.DEL_TEMP)
                .addBodyItem(tempId);

        try {
            Invoke invoke = new Invoke(proto, Server.getClient(communityId).getSocketList());
            List<Object> list = invoke.invoke();
            return list.stream().anyMatch(resp -> (Boolean)resp);
        }catch (Exception e){
        }

        return false;
    }

    @Override
    public byte[] getPhoto(Long communityId, String fileDirAndName) {
        return null;
    }
}
