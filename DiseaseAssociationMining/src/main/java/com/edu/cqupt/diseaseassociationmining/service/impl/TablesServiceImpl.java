package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.Tables;
import com.edu.cqupt.diseaseassociationmining.mapper.TablesMapper;
import com.edu.cqupt.diseaseassociationmining.service.TablesService;
import org.springframework.stereotype.Service;

@Service
public class TablesServiceImpl extends ServiceImpl<TablesMapper,Tables> implements TablesService{
}
