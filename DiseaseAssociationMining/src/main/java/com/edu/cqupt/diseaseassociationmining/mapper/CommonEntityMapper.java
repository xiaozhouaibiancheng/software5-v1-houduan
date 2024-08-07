package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.common.CommonEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommonEntityMapper extends BaseMapper<CommonEntity> {
    Integer findTargetColumnIndex(@Param("tablename") String tablename, @Param("target") String target);
}
