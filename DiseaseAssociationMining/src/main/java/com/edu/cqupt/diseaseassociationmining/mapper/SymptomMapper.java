package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.Association;
import com.edu.cqupt.diseaseassociationmining.entity.Symptom;
import org.springframework.stereotype.Repository;

@Repository
public interface SymptomMapper extends BaseMapper<Symptom> {
}
