package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.mapper.NodesMapper;
import com.edu.cqupt.diseaseassociationmining.entity.Nodes;
import com.edu.cqupt.diseaseassociationmining.entity.Relationships;
import com.edu.cqupt.diseaseassociationmining.service.NodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodesServiceImpl extends ServiceImpl<NodesMapper, Nodes>
        implements NodesService {
    @Autowired
    NodesMapper nodesMapper;


    @Override
    public List<Nodes> getAllNodes() {
        return nodesMapper.getAllNodes();
    }

    @Override
    public List<Relationships> getRelationships() {
        return nodesMapper.getRelationships();
    }
}
