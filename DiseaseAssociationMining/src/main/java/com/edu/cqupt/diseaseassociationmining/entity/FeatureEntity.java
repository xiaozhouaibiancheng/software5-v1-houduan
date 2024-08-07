package com.edu.cqupt.diseaseassociationmining.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// TODO 公共模块新增类
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("field_management")
public class FeatureEntity implements Serializable {
    @TableId
    private Integer characterId;

    private String featureName;
    private String chName;

    private boolean diseaseStandard = false;
    private boolean diagnosis = false;
    private boolean examine = false;
    private boolean pathology = false;
    private boolean vitalSigns = false;

    private String tableName;
    private String unit;
    private boolean isLabel = false;

}
