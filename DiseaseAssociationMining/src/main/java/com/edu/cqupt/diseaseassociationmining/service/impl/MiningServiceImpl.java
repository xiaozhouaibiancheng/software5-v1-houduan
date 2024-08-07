package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.Mining;
import com.edu.cqupt.diseaseassociationmining.mapper.MiningMapper;
import com.edu.cqupt.diseaseassociationmining.service.MiningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MiningServiceImpl extends ServiceImpl<MiningMapper, Mining> implements MiningService {
    @Resource
    MiningMapper miningMapper;
    @Override
    public PageInfo<Mining> findByPageService(int pageNum, int pageSize, QueryWrapper<Mining> queryWrapper) {
        PageHelper.startPage(pageNum,pageSize);
        List<Mining> taskInfos = miningMapper.selectList(queryWrapper);
        return new PageInfo<>(taskInfos);
    }
}
