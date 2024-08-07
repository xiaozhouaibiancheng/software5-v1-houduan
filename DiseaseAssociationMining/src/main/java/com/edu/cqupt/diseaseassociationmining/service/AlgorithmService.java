package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.Algorithm;
import com.github.pagehelper.PageInfo;

public interface AlgorithmService extends IService<Algorithm> {
    PageInfo<Algorithm> findByPageService(int pageNum, int pageSize, QueryWrapper<Algorithm> queryWrapper);
}
