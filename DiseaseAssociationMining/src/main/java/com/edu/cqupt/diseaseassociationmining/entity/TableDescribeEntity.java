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

// TODO 公共模块新增类

@TableName("table_describe")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TableDescribeEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private String id;
    private String tableId;
    private String tableName;
    private String createUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern ="yyyy-MM-dd", timezone = "GMT+8")
    private String createTime;
    private String classPath;
    private String uid;
    private String tableStatus;
    private Float tableSize;
    private String checkApproving;
    private String checkApproved;
    private static final long serialVersionUID = 1L;
}
