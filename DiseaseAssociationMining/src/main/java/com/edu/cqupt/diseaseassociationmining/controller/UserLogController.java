package com.edu.cqupt.diseaseassociationmining.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.UserLog;
import com.edu.cqupt.diseaseassociationmining.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userlog")
public class UserLogController {
    @Autowired
    private UserLogService userLogService;

    @GetMapping("/getLogByPage")
    public Result queryLogByPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam String username
    ){
        QueryWrapper<UserLog> queryWrapper = new QueryWrapper<UserLog>().orderByDesc("id");
        queryWrapper.like(StringUtils.isNotBlank(username),"username",username);

        Page<UserLog> page = userLogService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }
}
