package com.edu.cqupt.diseaseassociationmining.view;

import com.edu.cqupt.diseaseassociationmining.entity.Tables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResult {
    private String tableName;
    private List<String> tableHeaders;
    private List<Tables> res;
    private Integer code;
    private Exception e;

}