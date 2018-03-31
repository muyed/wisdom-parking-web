package com.muye.wp.service;

import com.muye.wp.dao.domain.CapitalFlow;

/**
 * Created by muye on 18/3/30.
 */
public interface CapitalFlowService {

    void add(CapitalFlow flow);

    CapitalFlow queryByOrderNum(String orderNum);

    void update(CapitalFlow flow);
}
