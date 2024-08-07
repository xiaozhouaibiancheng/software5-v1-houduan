package com.edu.cqupt.diseaseassociationmining.controller;

import com.edu.cqupt.diseaseassociationmining.common.R;
import com.edu.cqupt.diseaseassociationmining.entity.Notification;
import com.edu.cqupt.diseaseassociationmining.service.NoticeService;
import com.edu.cqupt.diseaseassociationmining.vo.InsertNoticeVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/allNotices")
    public PageInfo<Notification> allNotices(@RequestParam Integer pageNum , @RequestParam Integer pageSize){
        return noticeService.allNotices(pageNum, pageSize);
    }

    @GetMapping("/queryNotices")
    public List<Notification> queryNotices(){
        return noticeService.queryNotices();
    }




    @PostMapping("/updateNotice")
    public R updateNotice(@RequestBody Notification notification){
        Date date = new Date();
        System.out.println(new Date());

        notification.setUpdateTime(new Date());
        System.out.println(new Date());
        System.out.println(noticeService.saveOrUpdate(notification));

        return new R<>(200 , "成功", null);
    }


    @PostMapping("delNotice")
    public R delNotice(@RequestBody Notification notification){
        noticeService.removeById(notification.getInfoId());
        return new R<>(200 , "成功", null);
    }

    @PostMapping("insertNotice")
    public R insertNotice(@RequestBody InsertNoticeVo notification){

        noticeService.saveNotification(notification);
        return new R<>(200 , "成功", null);
    }


}