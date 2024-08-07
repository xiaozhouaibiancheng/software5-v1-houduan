package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.Notification;
import com.edu.cqupt.diseaseassociationmining.mapper.NoticeMapper;
import com.edu.cqupt.diseaseassociationmining.service.NoticeService;
import com.edu.cqupt.diseaseassociationmining.vo.InsertNoticeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notification>
        implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;


    @Override
    public PageInfo<Notification> allNotices(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 返回查询结果列表
        List<Notification> notifications =  noticeMapper.selectAllNotices();
        // 使用 PageInfo 包装查询结果，并返回
        return new PageInfo<>(notifications);
    }

    @Override
    public void saveNotification(InsertNoticeVo notification) {
        noticeMapper.saveNotification(notification);
    }

    @Override
    public List<Notification> queryNotices() {

        List<Notification> notifications = noticeMapper.selectList(null);
        return notifications;
    }
}
