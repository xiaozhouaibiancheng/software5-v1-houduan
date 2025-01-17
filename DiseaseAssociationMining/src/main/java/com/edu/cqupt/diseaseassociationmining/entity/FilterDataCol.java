package com.edu.cqupt.diseaseassociationmining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("filter_data_col")
public class FilterDataCol {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer characterId;
    private String featureName;
    private String chName;
    private String computeOpt;
    private String unit;
    private String type;
    private String value;
    private Integer opt;
    private Integer filterDataInfoId;
    private String range;

}
