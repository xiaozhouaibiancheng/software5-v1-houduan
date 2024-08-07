package com.edu.cqupt.diseaseassociationmining.vo;

import com.edu.cqupt.diseaseassociationmining.entity.FilterDataCol;
import com.edu.cqupt.diseaseassociationmining.entity.FilterDataInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterConditionVo {
    private FilterDataInfo filterDataInfo;
    private List<FilterDataCol> filterDataCols;
}
