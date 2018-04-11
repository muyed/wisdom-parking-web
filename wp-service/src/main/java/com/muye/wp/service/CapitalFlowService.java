package com.muye.wp.service;

import com.muye.wp.common.cons.CapitalFlowDirection;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.page.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 18/3/30.
 */
public interface CapitalFlowService {

    List<CapitalFlow> queryListByCondition(CapitalFlow query, Page page);

    void add(CapitalFlow flow);

    void add(Long userId, CapitalFlowDirection direction, ProductType type, String orderNum, BigDecimal amount);

    CapitalFlow queryByOrderNum(String orderNum);

    void update(CapitalFlow flow);
}
