package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.FieldManagementEntity;

import java.util.List;

// TODO 公共模块新增类
public interface FieldManagementService extends IService<FieldManagementEntity> {
    List<FieldManagementEntity> getFiledByDiseaseName(String diseaseName);

    void updateFieldsByDiseaseName(String diseaseName, List<String> fields);
}
