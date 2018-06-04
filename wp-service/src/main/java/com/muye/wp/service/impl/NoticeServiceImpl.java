package com.muye.wp.service.impl;

import com.muye.wp.dao.domain.Notice;
import com.muye.wp.dao.mapper.NoticeMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 18/6/4.
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public List<Notice> queryListByCondition(Notice query, Page page) {
        return noticeMapper.selectListByCondition(query, page);
    }

    @Override
    public void insert(Notice notice) {
        noticeMapper.insert(notice);
    }

    @Override
    public void update(Notice notice) {
        noticeMapper.update(notice);
    }
}
