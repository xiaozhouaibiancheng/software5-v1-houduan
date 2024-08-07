package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.Nodes;
import com.edu.cqupt.diseaseassociationmining.entity.Relationships;

import java.util.List;

public interface NodesService extends IService<Nodes> {
    List<Nodes> getAllNodes();

    List<Relationships> getRelationships();
}
