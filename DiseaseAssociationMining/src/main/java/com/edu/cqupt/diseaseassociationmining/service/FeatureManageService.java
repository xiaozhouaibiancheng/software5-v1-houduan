package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.FeatureEntity;
import com.edu.cqupt.diseaseassociationmining.vo.FeatureListVo;
import com.edu.cqupt.diseaseassociationmining.vo.FeatureVo;

import java.util.List;

// TODO 公共模块新增类
public interface FeatureManageService extends IService<FeatureEntity> {
    List<FeatureVo> getFeatureList(String belongType);
    List<FeatureVo> selectFeaturesContinue(String belongType);

    void insertFeatures(FeatureListVo featureListVo);

    List<String> getUserFeatureList( String tablename);
}
