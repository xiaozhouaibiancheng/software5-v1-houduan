package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// TODO 公共模块新增类

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
    void removeNode(@Param("id") String id);

    void removeTable(@Param("label") String label);
    void changeStatusToShare(@Param("id") String id);

    void changeStatusToPrivate(@Param("id") String id);


    List<CategoryEntity> getSpDisease();
    List<CategoryEntity> getComDisease();

    //    下面方法是管理员端-数据管理新增
    List<CategoryEntity> getLevel2Label();
    String getLabelByPid(@Param("pid") String pid);
    List<CategoryEntity> getLabelsByPid(@Param("pid") String pid);

    void updateTableNameByTableName(@Param("oldTableName") String oldTableName, @Param("tableName") String tableName, @Param("tableStatus") String tableStatus);
    void updateTableNameByTableId(@Param("tableid") String tableid, @Param("tableName") String tableName, @Param("tableStatus") String tableStatus);
}
