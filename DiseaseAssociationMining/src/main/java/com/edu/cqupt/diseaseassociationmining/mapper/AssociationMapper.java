package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.Association;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface AssociationMapper extends BaseMapper<Association> {
}
