package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.KnowledgeBase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface KnowledgeBaseMapper extends BaseMapper<KnowledgeBase> {
//    List<KnowledgeBase> getKnowledgeBaseList();
}
