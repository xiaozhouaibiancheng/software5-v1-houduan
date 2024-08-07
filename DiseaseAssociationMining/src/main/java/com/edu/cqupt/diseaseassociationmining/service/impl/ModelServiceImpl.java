package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.mapper.ModelMapper;
import com.edu.cqupt.diseaseassociationmining.entity.Model;
import com.edu.cqupt.diseaseassociationmining.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model>
        implements ModelService {
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<String> getModelNames() {
        return modelMapper.getModelNames();
    }
}
