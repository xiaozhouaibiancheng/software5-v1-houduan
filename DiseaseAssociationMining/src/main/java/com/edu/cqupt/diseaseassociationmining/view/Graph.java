package com.edu.cqupt.diseaseassociationmining.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Graph {
    private List<Nodes> nodes;
    private List<Links> links;
    private List<Categories> categories;
}
