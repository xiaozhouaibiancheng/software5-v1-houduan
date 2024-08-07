package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.User;
import com.edu.cqupt.diseaseassociationmining.entity.UserLog;
import com.edu.cqupt.diseaseassociationmining.mapper.UserLogMapper;
import com.edu.cqupt.diseaseassociationmining.mapper.UserMapper;
import com.edu.cqupt.diseaseassociationmining.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {
    @Autowired
    UserLogMapper logMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<UserLog> getAllLogs() {
        return logMapper.getAllLogs();
    }
    @Override
    public void insertLog(Integer uid, Integer role, String operation) {
        User user = userMapper.getUserById(uid);


        UserLog logEntity = new UserLog();
        logEntity.setUid(uid);
        logEntity.setUsername(user.getUsername());
//        logEntity.setRole(role);
        logEntity.setOpType(operation);
        // 创建 DateTimeFormatter 对象，定义日期时间的格式
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        // 创建 LocalDateTime 对象，存储当前日期和时间
//        LocalDateTime now = LocalDateTime.now();
//        // 使用 formatter 格式化 LocalDateTime 对象
//        String formattedDate = now.format(formatter);
//        logEntity.setOpTime(formattedDate);
        Date date = new Date();
        logEntity.setCreateTime(date);
        logMapper.insert(logEntity);
    }
}