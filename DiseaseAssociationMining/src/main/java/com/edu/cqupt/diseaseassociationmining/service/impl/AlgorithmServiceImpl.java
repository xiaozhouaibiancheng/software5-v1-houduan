package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.Algorithm;
import com.edu.cqupt.diseaseassociationmining.mapper.AlgorithmMapper;
import com.edu.cqupt.diseaseassociationmining.service.AlgorithmService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements AlgorithmService{
    @Resource
    AlgorithmMapper algorithmMapper;
    @Override
    public PageInfo<Algorithm> findByPageService(int pageNum, int pageSize, QueryWrapper<Algorithm> queryWrapper) {
        PageHelper.startPage(pageNum,pageSize);
        List<Algorithm> algorithms = algorithmMapper.selectList(queryWrapper);
        return new PageInfo<>(algorithms);
    }
}
