package com.muye.wp.service.impl;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.CommunityModule;
import com.muye.wp.dao.domain.query.CarportQuery;
import com.muye.wp.dao.mapper.CarportMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CarportService;
import com.muye.wp.service.CommunityModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by muye on 18/3/6.
 */
@Service
public class CarportServiceImpl implements CarportService {

    @Autowired
    private CarportMapper carportMapper;

    @Autowired
    private CommunityModuleService communityModuleService;

    @Override
    public void add(Carport carport) {

        CarportQuery query = new CarportQuery();
        query.setMeid(carport.getMeid());

        if (carportMapper.selectListByCondition(query, null).size() > 0)
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        CommunityModule module = communityModuleService.queryById(carport.getCommunityModuleId());
        if (module == null){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST, "小区模块不存在");
        }
        carport.setCommunityId(module.getCommunityId());
        carport.setBindCode(CommonUtil.genRandomNum(6));
        carport.setLockStatus(0);
        carport.setShareStatus(0);
        carportMapper.insert(carport);
    }

    @Override
    @Transactional
    public String refreshBindCode(Long id) {

        Carport carport = carportMapper.selectByIdForUpdate(id);
        if (carport == null){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        }
        String bindCode = CommonUtil.genRandomNum(6);
        carport.setBindCode(bindCode);
        carportMapper.update(carport);
        return bindCode;
    }

    @Override
    public List<Carport> queryListByCondition(CarportQuery query, Page page) {
        return carportMapper.selectListByCondition(query, page);
    }

    @Override
    public Carport queryById(Long id) {
        return carportMapper.selectById(id);
    }
}
