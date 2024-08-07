package com.edu.cqupt.diseaseassociationmining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("software4task")
public class Task implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String  taskname;
    private String  leader;
    private String  participant;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern ="yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
    private String   disease;
    private String   model;
    private String   remark;
    private String   feature;
    private String   result;
    private String   parameters;
    private String   parametersvalues;
    private String   targetcolumn;
    private double  usetime;
    private int     ci;
    private double  ratio;
    private String dataset;
    private String tips;
    private Integer userid;
    private String tasktype;
}
