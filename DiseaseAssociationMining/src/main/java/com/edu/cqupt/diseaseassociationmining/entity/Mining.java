package com.edu.cqupt.diseaseassociationmining.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.edu.cqupt.diseaseassociationmining.common.TaskType;
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

@TableName("mining")
public class Mining {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String creator;
    private TaskType type;
    private String description;
    private String disease;
    private String factor;
    private String tableName;
    private String algorithmName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern ="yyyy-MM-dd", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date time;
}
