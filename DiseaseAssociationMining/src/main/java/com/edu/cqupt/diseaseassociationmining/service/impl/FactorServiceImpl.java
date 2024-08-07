package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.mapper.FactorMapper;
import com.edu.cqupt.diseaseassociationmining.service.FactorService;
import com.edu.cqupt.diseaseassociationmining.entity.Factor;
import org.springframework.stereotype.Service;

@Service
public class FactorServiceImpl extends ServiceImpl<FactorMapper, Factor> implements FactorService {
}
