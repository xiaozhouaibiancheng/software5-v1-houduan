package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.Disease;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface DiseaseMapper extends BaseMapper<Disease> {
}
