package com.edu.cqupt.diseaseassociationmining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)

@TableName("algorithm")
public class Algorithm {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String algorithmName;
    private String taskType;
    private String algorithmDescription;
    private String deployFilePath;
    private String functionality;
    private String principle;
    private String useCase;
}