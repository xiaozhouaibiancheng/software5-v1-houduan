package com.edu.cqupt.diseaseassociationmining.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("public.user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO,value = "uid")
    private Integer uid;

    private String username;

    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern ="yyyy-MM-dd", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern ="yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    private Integer role;
    @TableField(exist = false)
    private String code;
    private String userStatus;
    @TableField("answer_1")
    private String answer1;
    @TableField("answer_2")
    private String answer2;
    @TableField("answer_3")
    private String answer3;
    private Double uploadSize;

    private static final long serialVersionUID = 1L;
}