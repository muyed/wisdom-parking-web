package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.Notice;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.NoticeService;
import com.muye.wp.wap.security.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by muye on 18/6/4.
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Auth({UserType.GENERAL, UserType.OPERATOR, UserType.PROPERTY})
    @GetMapping("/list")
    public Result<List<Notice>> list(Notice query, Page page){
        return Result.ok(noticeService.queryListByCondition(query, page));
    }

    @Auth(UserType.OPERATOR)
    @PostMapping("/add")
    public Result add(Notice notice){
        noticeService.insert(notice);
        return Result.ok();
    }

    @Auth(UserType.OPERATOR)
    @PostMapping("/update")
    public Result update(Notice notice){
        noticeService.update(notice);
        return Result.ok();
    }

}
