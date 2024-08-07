package com.edu.cqupt.diseaseassociationmining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.cqupt.diseaseassociationmining.common.FactorCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)

@TableName("factor")
public class Factor {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private FactorCategory category;
}
