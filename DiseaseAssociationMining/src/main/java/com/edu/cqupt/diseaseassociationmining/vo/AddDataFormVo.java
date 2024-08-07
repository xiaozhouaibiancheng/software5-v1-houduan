package com.edu.cqupt.diseaseassociationmining.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO 公共模块新增类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDataFormVo {
    private String dataType;
    private String diseaseName;
    private String username;
    private Integer uid;
    private String isFilter;

    private String isUpload;


    private String dataName;
    private String createUser;
    private String uid_list;
    private List<CreateTableFeatureVo> characterList;
}
