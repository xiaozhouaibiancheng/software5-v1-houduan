package com.edu.cqupt.diseaseassociationmining.controller;

import com.edu.cqupt.diseaseassociationmining.entity.Association;
import com.edu.cqupt.diseaseassociationmining.entity.Disease;
import com.edu.cqupt.diseaseassociationmining.entity.Factor;
import com.edu.cqupt.diseaseassociationmining.service.AssociationService;
import com.edu.cqupt.diseaseassociationmining.service.DiseaseService;
import com.edu.cqupt.diseaseassociationmining.service.FactorService;
import com.edu.cqupt.diseaseassociationmining.service.KnowledgeBaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {
    @Resource
    AssociationService associationService;
    @Resource
    DiseaseService diseaseService;
    @Resource
    FactorService factorService;
    @Resource
    KnowledgeBaseService knowledgeBaseService;

    @PostMapping("/getDisease")
    public List<Disease> getDiseaseNum(){
        return diseaseService.list();
    }
    @PostMapping("/getFactor")
    public List<Factor> getFactor(){
        return factorService.list();
    }
    @PostMapping("/getAssociation")
    public  List<Association> getAssociation(){
        return associationService.list();
    }
    @PostMapping("/getKnowledgeNumber")
    public  int getKnowledgeNumber(){
        return knowledgeBaseService.list().size();
    }

}
