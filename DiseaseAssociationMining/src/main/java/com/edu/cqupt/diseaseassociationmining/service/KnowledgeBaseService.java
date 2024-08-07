package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.KnowledgeBase;
import com.github.pagehelper.PageInfo;

public interface KnowledgeBaseService extends IService<KnowledgeBase> {
//    List<KnowledgeBase> getKnowledgeBaseList();
    PageInfo<KnowledgeBase> findByPageService(int pageNum, int pageSize, QueryWrapper<KnowledgeBase> queryWrapper);
    boolean updateKnowledgeBase(KnowledgeBase knowledgeBase);
}
