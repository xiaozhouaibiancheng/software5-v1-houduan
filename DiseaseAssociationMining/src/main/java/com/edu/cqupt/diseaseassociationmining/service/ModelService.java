package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.Model;

import java.util.List;

public interface ModelService extends IService<Model> {
    List<String> getModelNames();
}
