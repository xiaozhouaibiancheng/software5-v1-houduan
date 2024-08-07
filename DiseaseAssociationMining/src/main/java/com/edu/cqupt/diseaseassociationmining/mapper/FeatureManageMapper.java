package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.FeatureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// TODO 公共模块新增类

@Mapper
public interface FeatureManageMapper extends BaseMapper<FeatureEntity> {
    List<FeatureEntity> selectFeatures(@Param("belongType") String belongType);

    List<String> getUserFeatureList(String tablename);
    List<FeatureEntity> selectFeaturesContinue(@Param("belongType") String belongType);
}
