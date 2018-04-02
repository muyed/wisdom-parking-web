package com.muye.wp.service.impl;

import com.muye.wp.dao.domain.CarLicense;
import com.muye.wp.dao.mapper.CarLicenseMapper;
import com.muye.wp.service.CarLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 18/4/2.
 */
@Service
public class CarLicenseServiceImpl implements CarLicenseService {

    @Autowired(required = false)
    private CarLicenseMapper carLicenseMapper;

    @Override
    public List<CarLicense> queryListByUserId(Long userId) {
        return carLicenseMapper.selectListByUserId(userId);
    }

    @Override
    public void add(CarLicense carLicense) {
        carLicenseMapper.insert(carLicense);
    }

    @Override
    public void delete(Long id, Long userId) {
        carLicenseMapper.delete(id, userId);
    }
}
