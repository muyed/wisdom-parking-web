package com.muye.wp.service;

import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.query.CarportQuery;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/3/6.
 */
public interface CarportService {

    void add(Carport carport);

    String refreshBindCode(Long id);

    List<Carport> queryListByCondition(CarportQuery query, Page page);

    Carport queryById(Long id);
}
