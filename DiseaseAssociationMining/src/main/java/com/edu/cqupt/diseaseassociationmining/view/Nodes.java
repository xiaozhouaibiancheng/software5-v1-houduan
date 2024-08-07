package com.edu.cqupt.diseaseassociationmining.view;

import com.edu.cqupt.diseaseassociationmining.common.DiseaseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Nodes {
    private Integer id;
    private String name;
    private Float symbolSize;
    private Float value;
    private Integer category;
}
