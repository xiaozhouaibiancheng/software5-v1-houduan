package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.Mining;
import com.github.pagehelper.PageInfo;

public interface MiningService extends IService<Mining> {
    PageInfo<Mining> findByPageService(int pageNum, int pageSize, QueryWrapper<Mining> queryWrapper);
}
