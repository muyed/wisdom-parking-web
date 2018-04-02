package com.muye.wp.service;

import com.muye.wp.dao.domain.CarLicense;

import java.util.List;

/**
 * Created by muye on 18/4/2.
 */
public interface CarLicenseService {

    List<CarLicense> queryListByUserId(Long userId);

    void add(CarLicense carLicense);

    void delete(Long id, Long userId);
}
