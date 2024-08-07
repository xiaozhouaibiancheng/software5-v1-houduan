package com.edu.cqupt.diseaseassociationmining.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KnowledgeInsertVO {
    private String diseaseName;
    private String diseaseCategory;
    private String factorName;
    private String factorCategory;
    private Float weight;
    private Integer uid;
    private String username;
    private String status;
}
