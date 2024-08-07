package com.edu.cqupt.diseaseassociationmining.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node implements Serializable {
//    @TableId(value = "Id", type = IdType.AUTO)
    private Integer id;
    private String path;
    private String label;
    private boolean isLeafs;
    private boolean isCommon;
    private List<Node> children;
}
