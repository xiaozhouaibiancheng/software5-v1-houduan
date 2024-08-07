package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.UserLog;

import java.util.List;

public interface UserLogService extends IService<UserLog> {
    List<UserLog> getAllLogs();
    void insertLog(Integer uid, Integer role, String operation);
}