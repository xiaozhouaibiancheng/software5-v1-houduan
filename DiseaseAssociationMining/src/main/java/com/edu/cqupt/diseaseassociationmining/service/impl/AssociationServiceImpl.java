package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.Association;
import com.edu.cqupt.diseaseassociationmining.mapper.AssociationMapper;
import com.edu.cqupt.diseaseassociationmining.service.AssociationService;
import org.springframework.stereotype.Service;

@Service
public class AssociationServiceImpl extends ServiceImpl<AssociationMapper, Association> implements AssociationService {
}
