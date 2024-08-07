package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.Notification;
import com.edu.cqupt.diseaseassociationmining.vo.InsertNoticeVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hp
* @description 针对表【Notification】的数据库操作Mapper
* @createDate 2023-05-16 16:44:39
* @Entity com.cqupt.software_1.entity.User
*/


@Mapper
public interface NoticeMapper extends BaseMapper<Notification> {




    Page<Notification> selectAllNotices();

    void saveNotification(InsertNoticeVo notification);
}




