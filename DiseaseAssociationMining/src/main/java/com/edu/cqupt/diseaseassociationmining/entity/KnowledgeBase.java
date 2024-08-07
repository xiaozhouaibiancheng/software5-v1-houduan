package com.edu.cqupt.diseaseassociationmining.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)

@TableName("knowledgebase")
public class KnowledgeBase {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer source;
    private String diseaseName;
    private Integer target;
    private String factorName;
    private Boolean is_new;
    private Float weight;
    private Integer uid;
    private String createUser;
    //0、1、2分别代表审核、通过、拒绝
    private String status;
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern ="yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}