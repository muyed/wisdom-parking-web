package com.muye.wp.service;

import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/3/30.
 */
public interface CapitalFlowService {

    List<CapitalFlow> queryListByCondition(CapitalFlow query, Page page);

    void add(CapitalFlow flow);

    CapitalFlow queryByOrderNum(String orderNum);

    void update(CapitalFlow flow);
}
