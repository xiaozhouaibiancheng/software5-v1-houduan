package com.edu.cqupt.diseaseassociationmining.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Links {
    private String source;
    private String target;
    private String value;
    private Boolean isNew;
    private LineStyle lineStyle;
}
