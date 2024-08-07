package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.Category2Entity;

import java.util.List;

// TODO 公共模块新增类
public interface Category2Service extends IService<Category2Entity> {

    List<Category2Entity> getCategory();

    List<Category2Entity> getCategory2();
    void removeNode(String id);
}
