package com.muye.wp.service;

import com.muye.wp.dao.domain.Notice;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/6/4.
 */
public interface NoticeService {

    List<Notice> queryListByCondition(Notice query, Page page);

    void insert(Notice notice);

    void update(Notice notice);
}
