package com.edu.cqupt.diseaseassociationmining.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.edu.cqupt.diseaseassociationmining.common.DiseaseCategory;
import com.edu.cqupt.diseaseassociationmining.common.FactorCategory;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.Disease;
import com.edu.cqupt.diseaseassociationmining.entity.Factor;
import com.edu.cqupt.diseaseassociationmining.entity.KnowledgeBase;
import com.edu.cqupt.diseaseassociationmining.entity.UserLog;
import com.edu.cqupt.diseaseassociationmining.mapper.KnowledgeBaseMapper;
import com.edu.cqupt.diseaseassociationmining.service.DiseaseService;
import com.edu.cqupt.diseaseassociationmining.service.FactorService;
import com.edu.cqupt.diseaseassociationmining.service.KnowledgeBaseService;
import com.edu.cqupt.diseaseassociationmining.service.UserLogService;
import com.edu.cqupt.diseaseassociationmining.view.Categories;
import com.edu.cqupt.diseaseassociationmining.view.Graph;
import com.edu.cqupt.diseaseassociationmining.view.Links;
import com.edu.cqupt.diseaseassociationmining.view.Nodes;
import com.edu.cqupt.diseaseassociationmining.vo.KnowledgeInsertVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/knowledgeBase")
public class KnowledgeBaseController {
    @Resource
    KnowledgeBaseService knowledgeBaseService;
    @Resource
    KnowledgeBaseMapper knowledgeBaseMapper;
    @Resource
    DiseaseService diseaseService;
    @Resource
    FactorService factorService;
    @Resource
    UserLogService userLogService;

    @GetMapping("/all")
    public Result getKnowledgeBaseList() {
        return Result.success(knowledgeBaseService.list());
    }
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String diseaseName,
                               @RequestParam String factorName,
                               @RequestParam String username){
        QueryWrapper<KnowledgeBase> queryWrapper = new QueryWrapper<KnowledgeBase>().orderByDesc("create_time");
        queryWrapper.like(StringUtils.isNotBlank(username),"create_user",username);
        queryWrapper.like(StringUtils.isNotBlank(diseaseName),"disease_name","%"+diseaseName+"%");
        queryWrapper.like(StringUtils.isNotBlank(factorName),"factor_name","%"+factorName+"%");
        queryWrapper.eq("status","1");
        PageInfo<KnowledgeBase> pageInfo = knowledgeBaseService.findByPageService(pageNum, pageSize,queryWrapper);
        return Result.success(pageInfo);
    }
    @GetMapping("/getApprove")
    public  Result getApprove(){
        QueryWrapper<KnowledgeBase> queryWrapper = new QueryWrapper<KnowledgeBase>().orderByDesc("create_time");
        queryWrapper.eq("status","0");
        List<KnowledgeBase> knowledgeBases = knowledgeBaseMapper.selectList(queryWrapper);
        return Result.success(knowledgeBases);
    }
    @PostMapping("/insert")
    public  Result knowledgeBaseInsert(@RequestBody List<KnowledgeInsertVO> knowledgeBases){
        String messige = "";
        int code = 200;
        int flag = 0;
        for(KnowledgeInsertVO knowledgeBase:knowledgeBases){
            List<Disease> disease = diseaseService.lambdaQuery()
                    .eq(Disease::getName,knowledgeBase.getDiseaseName())
                    .list();
            List<Factor> factor = factorService.lambdaQuery()
                    .eq(Factor::getName,knowledgeBase.getFactorName())
                    .list();
            List<KnowledgeBase> knowledge = knowledgeBaseService.lambdaQuery()
                    .eq(KnowledgeBase::getDiseaseName,knowledgeBase.getDiseaseName())
                    .eq(KnowledgeBase::getFactorName,knowledgeBase.getFactorName())
                    .list();
            if (disease.isEmpty()){
                Disease disease1 = new Disease();
                disease1.setName(knowledgeBase.getDiseaseName());
                disease1.setCategory(DiseaseCategory.valueOf(knowledgeBase.getDiseaseCategory()));
                diseaseService.save(disease1);
            }
            if(factor.isEmpty()){
                Factor factor1 = new Factor();
                factor1.setName(knowledgeBase.getFactorName());
                factor1.setCategory(FactorCategory.valueOf(knowledgeBase.getFactorCategory()));
                factorService.save(factor1);
            }
            if(knowledge.isEmpty()){
                KnowledgeBase knowledgeBase1 = new KnowledgeBase();
                knowledgeBase1.setSource(diseaseService.lambdaQuery()
                        .eq(Disease::getName,knowledgeBase.getDiseaseName())
                        .list().get(0).getId());
                knowledgeBase1.setDiseaseName(knowledgeBase.getDiseaseName());
                knowledgeBase1.setTarget(factorService.lambdaQuery()
                        .eq(Factor::getName,knowledgeBase.getFactorName())
                        .list().get(0).getId());
                knowledgeBase1.setFactorName(knowledgeBase.getFactorName());
                knowledgeBase1.setIs_new(false);
                knowledgeBase1.setWeight(knowledgeBase.getWeight());
                knowledgeBase1.setUid(knowledgeBase.getUid());
                knowledgeBase1.setCreateUser(knowledgeBase.getUsername());
                knowledgeBase1.setStatus(knowledgeBase.getStatus());
                knowledgeBase1.setCreateTime(new Date());
                knowledgeBaseService.save(knowledgeBase1);
                UserLog userLog = new UserLog(null,knowledgeBase.getUid(),new Date(),"新增关联："+knowledgeBase.getDiseaseName()+"-"+knowledgeBase.getFactorName(),knowledgeBase.getUsername());
                userLogService.save(userLog);
            }else{
                flag = 1;
                messige += knowledgeBase.getDiseaseName()+"--"+knowledgeBase.getFactorName()+",";
                code = 400;
            }
        }
        if(flag == 0)
            messige = "插入成功！";
        else if (flag == 1) {
            messige = "知识库中已存在关联："+messige;
            messige = messige.substring(0, messige.length() - 1);
        }
        return new Result<>(messige,code);
    }
    @PostMapping("/update")
    public Result updateKnowledgeBase(@RequestBody KnowledgeBase knowledgeBase) {
        knowledgeBase.setCreateTime(new Date());
        UserLog userLog = new UserLog(null,knowledgeBase.getUid(),new Date(),"修改关联："+knowledgeBase.getDiseaseName()+"-"+knowledgeBase.getFactorName(),knowledgeBase.getCreateUser());
        userLogService.save(userLog);
        return knowledgeBaseService.updateKnowledgeBase(knowledgeBase)?Result.success(200,"修改成功"):Result.fail("修改失败");
    }
    @GetMapping("/delete")
    public Result deleteKnowledgeBase(@RequestParam Integer knowledgeBaseId,
                                      @RequestParam String diseaseName,
                                      @RequestParam String factorName,
                                      @RequestParam Integer uid,
                                      @RequestParam String username){
        UserLog userLog = new UserLog(null,uid,new Date(),"删除关联："+diseaseName+"-"+factorName,username);
        userLogService.save(userLog);
        QueryWrapper<KnowledgeBase> queryWrapper  =new QueryWrapper<>();
        queryWrapper.eq("id",knowledgeBaseId);
        return knowledgeBaseService.remove(queryWrapper)?Result.success(200,"删除成功"):Result.fail(400,"删除失败");
    }
    @GetMapping("/underCheckApprove")
    public Result underCheckeApprove(@RequestParam Integer knowledgeBaseId,
                               @RequestParam Integer uid,
                               @RequestParam String username){
            KnowledgeBase knowledgeBase = knowledgeBaseService.getById(knowledgeBaseId);
            knowledgeBase.setStatus("1");
            QueryWrapper<KnowledgeBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",knowledgeBaseId);
            Boolean flag = knowledgeBaseService.update(knowledgeBase, queryWrapper);
        UserLog userLog = new UserLog(null,uid,new Date(),"通过关联审核："+knowledgeBaseId,username);
        userLogService.save(userLog);
        return flag?Result.success(200,"通过关联审核："+knowledgeBase):Result.fail(400,"通过审核失败");
    }
    @GetMapping("/denyCheckApprove")
    public Result denyCheckeApprove(@RequestParam Integer knowledgeBaseId,
                               @RequestParam Integer uid,
                               @RequestParam String username){
        UserLog userLog = new UserLog(null,uid,new Date(),"拒绝关联审核："+knowledgeBaseId,username);
        userLogService.save(userLog);
        QueryWrapper<KnowledgeBase> queryWrapper  =new QueryWrapper<>();
        queryWrapper.eq("id",knowledgeBaseId);
        return knowledgeBaseService.remove(queryWrapper)?Result.success(200,"已删除该关联"):Result.fail(400,"删除关联失败");
    }
    @GetMapping("/getGraph")
    public Result getGraph(){
        List<Disease> disease = diseaseService.list();
        List<Factor> factor = factorService.list();
        List<KnowledgeBase> knowledgeBases = knowledgeBaseService.list();
        Integer lenD = disease.size();
        Integer lenF = factor.size();
        List<Nodes> nodes = new ArrayList<>();
        List<Links> links = new ArrayList<>();
        List<Categories> categories = new ArrayList<>();

        Graph graph = new Graph();
        int i;
        for(i=0;i<lenD;i++){
            com.edu.cqupt.diseaseassociationmining.view.Nodes node = new com.edu.cqupt.diseaseassociationmining.view.Nodes();
            node.setId(i);
            node.setName(disease.get(i).getName());
            String cat = String.valueOf(disease.get(i).getCategory());
            DiseaseCategory d = DiseaseCategory.valueOf(cat);
            node.setCategory(d.ordinal());
            nodes.add(node);
        }
        int j;
        for(j=i;j<lenF+i;j++){
            com.edu.cqupt.diseaseassociationmining.view.Nodes node = new Nodes();
            node.setId(j);
            node.setName(factor.get(j-i).getName());
            String cat = String.valueOf(factor.get(j-i).getCategory());
            FactorCategory f = FactorCategory.valueOf(cat);
            node.setCategory(f.ordinal()+6);
            nodes.add(node);
        }
//            设置关联边
        for(KnowledgeBase knowledgeBase:knowledgeBases){
            Links link = new Links();
            link.setSource(String.valueOf(knowledgeBase.getSource()));
            link.setTarget(String.valueOf(knowledgeBase.getTarget()));
            link.setValue(String.valueOf(knowledgeBase.getWeight()));
            link.setIsNew(false);
            links.add(link);
        }
        DiseaseCategory[] diseaseCategories = DiseaseCategory.values();
        FactorCategory[] factorCategories = FactorCategory.values();
//            设置类型
        for(DiseaseCategory e:diseaseCategories){
            Categories category = new Categories();
            category.setName("Disease:"+e.toString());
            categories.add(category);
        }
        for(FactorCategory e:factorCategories){
            Categories category = new Categories();
            category.setName("Factor:"+e.toString());
            categories.add(category);
        }
        graph.setNodes(nodes);
        graph.setLinks(links);
        graph.setCategories(categories);
        System.out.println(graph);
        return Result.success(graph);
    }
}
