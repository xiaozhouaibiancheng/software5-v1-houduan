package com.edu.cqupt.diseaseassociationmining.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDiseaseVo {
    private Integer catLevel;
    private String firstDisease;
    private String icdCode;
//    private String secondDisease;
    private String parentId;
    private String username;
    private String uid;
}
