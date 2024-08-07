package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.Disease;
import com.edu.cqupt.diseaseassociationmining.entity.Factor;
import com.edu.cqupt.diseaseassociationmining.entity.KnowledgeBase;
import com.edu.cqupt.diseaseassociationmining.mapper.DiseaseMapper;
import com.edu.cqupt.diseaseassociationmining.mapper.FactorMapper;
import com.edu.cqupt.diseaseassociationmining.mapper.KnowledgeBaseMapper;
import com.edu.cqupt.diseaseassociationmining.service.KnowledgeBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseService {
    @Resource
    KnowledgeBaseMapper knowledgeBaseMapper;
    @Resource
    DiseaseMapper diseaseMapper;
    @Resource
    FactorMapper factorMapper;
    @Override
    public PageInfo<KnowledgeBase> findByPageService(int pageNum, int pageSize, QueryWrapper<KnowledgeBase> queryWrapper) {
        PageHelper.startPage(pageNum,pageSize);
        List<KnowledgeBase> knowledgeBases = knowledgeBaseMapper.selectList(queryWrapper);
        return new PageInfo<>(knowledgeBases);
    }
    @Override
    public boolean updateKnowledgeBase(KnowledgeBase knowledgeBase){
        //查找关联看是否修改了名称，若修改了需要将知识库、疾病表、危险因素表中所有相关字段修改
        boolean flag = true;
        KnowledgeBase knowledgeBaseOrigin = knowledgeBaseMapper.selectById(knowledgeBase.getId());
        if(!knowledgeBaseOrigin.getDiseaseName().equals(knowledgeBase.getDiseaseName())){
            UpdateWrapper<KnowledgeBase> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("disease_name", knowledgeBase.getDiseaseName()).eq("disease_name", knowledgeBaseOrigin.getDiseaseName());
            if(knowledgeBaseMapper.update(null, updateWrapper)==0)
                flag=false;
            UpdateWrapper<Disease> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.set("name", knowledgeBase.getDiseaseName()).eq("name", knowledgeBaseOrigin.getDiseaseName());
            if(diseaseMapper.update(null, updateWrapper1)==0)
                flag=false;
        }
        if (!knowledgeBaseOrigin.getFactorName().equals(knowledgeBase.getFactorName())) {
            UpdateWrapper<KnowledgeBase> updateWrapper2 = new UpdateWrapper<>();
            updateWrapper2.set("factor_name", knowledgeBase.getFactorName()).eq("factor_name", knowledgeBaseOrigin.getFactorName());
            if(knowledgeBaseMapper.update(null, updateWrapper2)==0)
                flag=false;
            UpdateWrapper<Factor> updateWrapper3 = new UpdateWrapper<>();
            updateWrapper3.set("name", knowledgeBase.getFactorName()).eq("name", knowledgeBaseOrigin.getFactorName());
            if(factorMapper.update(null, updateWrapper3)==0)
                flag=false;
        }
            UpdateWrapper<KnowledgeBase> updateWrapper4 = new UpdateWrapper<>();
            updateWrapper4.set("weight", knowledgeBase.getWeight()).eq("id", knowledgeBase.getId());
            if(knowledgeBaseMapper.update(null, updateWrapper4)==0)
                flag=false;
        return flag;
    }
}
