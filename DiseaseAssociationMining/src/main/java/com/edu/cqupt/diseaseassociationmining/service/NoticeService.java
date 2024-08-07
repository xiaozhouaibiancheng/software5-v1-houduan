package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.Notification;
import com.edu.cqupt.diseaseassociationmining.vo.InsertNoticeVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface NoticeService extends IService<Notification> {
    PageInfo<Notification> allNotices(Integer pageNum, Integer pageSize);

    void saveNotification(InsertNoticeVo notification);

    List<Notification> queryNotices();

}
