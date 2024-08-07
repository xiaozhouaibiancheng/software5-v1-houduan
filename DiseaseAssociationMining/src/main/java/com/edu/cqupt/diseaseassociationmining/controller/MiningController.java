package com.edu.cqupt.diseaseassociationmining.controller;

//import com.alibaba.fastjson.JSONArray;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.cqupt.diseaseassociationmining.common.DiseaseCategory;
import com.edu.cqupt.diseaseassociationmining.common.FactorCategory;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.common.TaskType;
import com.edu.cqupt.diseaseassociationmining.entity.*;
import com.edu.cqupt.diseaseassociationmining.service.*;
import com.edu.cqupt.diseaseassociationmining.tool.PythonRun;
import com.edu.cqupt.diseaseassociationmining.view.*;
import com.edu.cqupt.diseaseassociationmining.view.Nodes;
import com.github.pagehelper.PageInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/mining")
public class MiningController {
    @Resource
    private MiningService miningService;
    @Resource
    TableDataService tableDataService;
    @Resource
    private PythonRun pythonRun;
    @Resource
    private DiseaseService diseaseService;
    @Resource
    private FactorService factorService;
    @Resource
    private AssociationService  associationService;
    @Value("${gorit.file.root.path1}")
    private String path1;
    @Value("${gorit.file.root.path2}")
    private String path2;
    @Value("${spring.datasource.host}")
    private String host;
    @Value("${spring.datasource.dbname}")
    private String dbname;
    @Value("${spring.datasource.port}")
    private String port;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.username}")
    private String user;

    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String leader,
                               @RequestParam String dataset,
                               @RequestParam String tasktype,
                               @RequestParam String searchTask){

        QueryWrapper<Mining> queryWrapper = new QueryWrapper<Mining>().orderByDesc("time");
        queryWrapper.like(StringUtils.isNotBlank(leader),"creator",leader);
        queryWrapper.like(StringUtils.isNotBlank(dataset),"table_name",dataset);
        queryWrapper.eq(StringUtils.isNotBlank(tasktype),"type",tasktype);
        queryWrapper.like(StringUtils.isNotBlank(searchTask), "name", "%" + searchTask + "%");
        PageInfo<Mining> pageInfo = miningService.findByPageService(pageNum, pageSize,queryWrapper);
        return Result.success(pageInfo);
    }

    @PostMapping("/update")
    public Boolean miningUpdate(@RequestBody Mining mining) {
        return miningService.updateById(mining);
    }
    @PostMapping("/insert")
    public  Boolean miningInsert(@RequestBody Mining mining){
        return miningService.save(mining);
    }

    @GetMapping("/getByPage/{current}/{limit}")
    public Result findTables(@PathVariable long current,
                             @PathVariable long limit){
        //创建page对象，传递当前页，每页记录数
        Page<Mining> page = new Page<>(current, limit);
        //调用方法实现分页查询
        Page<Mining> page1 = miningService.page(page, null);
        return Result.success(page1);
    }
    @GetMapping("/getById/{miningId}")
    public Mining getById(@PathVariable int miningId){
        return miningService.getById(miningId);
    }

    @PostMapping("/getAll")
    public List<Mining> getAll(){
        return miningService.list();
    }

    @GetMapping("/getByName/{current}/{limit}/{miningName}")
    public Result getByName(@PathVariable long current,
                                  @PathVariable long limit,@PathVariable String miningName){
        Page<Mining> page = new Page<>(current, limit);
        QueryWrapper<Mining> wrapper = new QueryWrapper<>();
        wrapper.eq("name",miningName);
        //调用方法实现分页查询
        Page<Mining> page1 = miningService.page(page, wrapper);
        System.out.println(page1);
        return Result.success(page1);
    }
    @GetMapping("/getByName/{miningName}")
    public List<Mining> getByName(@PathVariable String miningName){
        List<Mining> mining = miningService.lambdaQuery()
                .eq(Mining::getName,miningName)
                .list();
        return mining;
    }
    @GetMapping("/getByType/{current}/{limit}/{miningType}")
    public Result getByType(@PathVariable long current,
                            @PathVariable long limit,@PathVariable String miningType){
        Page<Mining> page = new Page<>(current, limit);
        QueryWrapper<Mining> wrapper = new QueryWrapper<>();
        wrapper.eq("type",miningType);
        //调用方法实现分页查询
        Page<Mining> page1 = miningService.page(page, wrapper);
        return Result.success(page1);
    }
    @GetMapping("/getByType/{miningType}")
    public List<Mining> getByType(@PathVariable String miningType){
        List<Mining> mining = miningService.lambdaQuery()
                .eq(Mining::getType,miningType)
                .list();
        return mining;
    }
    @GetMapping("/getByCreator/{current}/{limit}/{miningCreator}")
    public Result getByCreator(@PathVariable long current,
                                     @PathVariable long limit,@PathVariable String miningCreator){
        Page<Mining> page = new Page<>(current, limit);
        QueryWrapper<Mining> wrapper = new QueryWrapper<>();
        wrapper.eq("creator",miningCreator);
        //调用方法实现分页查询
        Page<Mining> page1 = miningService.page(page, wrapper);
        return Result.success(page1);
    }
    @GetMapping("/getByCreator/{miningCreator}")
    public List<Mining> getByCreator(@PathVariable String miningCreator){
        List<Mining> mining = miningService.lambdaQuery()
                .eq(Mining::getCreator,miningCreator)
                .list();
        return mining;
    }
    @GetMapping("/getByTableName/{current}/{limit}/{tableName}")
    public Result getByTableName(@PathVariable long current,
                                       @PathVariable long limit,@PathVariable String tableName){
        Page<Mining> page = new Page<>(current, limit);
        QueryWrapper<Mining> wrapper = new QueryWrapper<>();
        wrapper.eq("table_name",tableName);
        //调用方法实现分页查询
        Page<Mining> page1 = miningService.page(page, wrapper);
        return Result.success(page1);

    }
    @GetMapping("/getByTableName/{tableName}")
    public List<Mining> getByTableName(@PathVariable String tableName){
                List<Mining> mining = miningService.lambdaQuery()
                .eq(Mining::getTableName,tableName)
                .list();
        return mining;
    }
    @GetMapping("/delete/{miningId}")
    public boolean miningDelete(@PathVariable int miningId){
        QueryWrapper<Mining> queryWrapper  =new QueryWrapper<>();
        queryWrapper.eq("id",miningId);
        return miningService.remove(queryWrapper);
    }
    @GetMapping("/getDiseaseFactorAssociation/{tableName}")
    public DiseaseFactorAssociation getDiseaseFactorAssociation(@PathVariable String tableName){
        //        动态读表
        DiseaseFactorAssociation diseaseFactorAssociation = new DiseaseFactorAssociation();
        List<LinkedHashMap<String, Object>> tableData = tableDataService.getAllTableData(tableName);
        System.out.println(tableData);

        //获取疾病、危险因素列表、关联关系
        List<Disease> disease = new ArrayList<>();
        LinkedHashMap<Object,Integer> d = new LinkedHashMap<>();
        List<Factor> factor = new ArrayList<>();
        LinkedHashMap<Object,Integer> f = new LinkedHashMap<>();
        List<Association> association = new ArrayList<>();
        //        获取不重合疾病列表
        Integer associationId = 1;
        Integer factorId = 1;
        Integer diseaseId = 1;
        for(LinkedHashMap<String,Object> a:tableData){
            if(!d.containsKey(a.get("disease_name"))){
                d.put(a.get("disease_name"),diseaseId++);
            }
            if(!f.containsKey(a.get("factor_name"))){
                f.put(a.get("factor_name"),factorId++);
            }
            Association temp = new Association(associationId++,d.get(a.get("disease_name")),f.get(a.get("factor_name")),false,1F);
            association.add(temp);
        }
        diseaseFactorAssociation.setAssociations(association);
        for (Map.Entry<Object, Integer> a : d.entrySet()) {
            System.out.println("Key: " + a.getKey() + ", Value: " + a.getValue());
            Disease temp;
            temp = diseaseService.lambdaQuery()
                    .eq(Disease::getName,a.getKey())
                    .one();
            Disease temp1 = new Disease();
            temp1.setId(a.getValue());
            temp1.setName(a.getKey().toString());
            if(temp!=null) {
                temp1.setCategory(temp.getCategory());
            }else{
                temp1.setCategory(DiseaseCategory.valueOf("others"));
            }
            disease.add(temp1);
        }
        diseaseFactorAssociation.setDiseases(disease);
//获取不重合症状列表

        for (Map.Entry<Object, Integer> a : f.entrySet()) {
            System.out.println("Key: " + a.getKey() + ", Value: " + a.getValue());
            Factor temp;
            temp = factorService.lambdaQuery()
                    .eq(Factor::getName,a.getKey())
                    .one();
            Factor temp1 = new Factor();
            temp1.setId(a.getValue());
            temp1.setName(a.getKey().toString());
            if(temp!=null) {
                temp1.setCategory(temp.getCategory());
            }else{
                temp1.setCategory(FactorCategory.valueOf("others"));
            }
            factor.add(temp1);
        }
        diseaseFactorAssociation.setFactors(factor);
        System.out.println(diseaseFactorAssociation);
        return diseaseFactorAssociation;
    }
//    @GetMapping("/getDisease/{tableName}")
//    public List<Disease> getDisease(@PathVariable String tableName){
////        从文件中读取疾病列表
//        List<Disease> disease = new ArrayList<>();
//        try{
//            String encoding = "UTF-8";
//            String filePath = path1+tableName+"_disease.txt";
//            File file = new File(filePath);
//            if(file.isFile()&&file.exists()){
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file),encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                                     String lineTxt = null;
//                lineTxt = bufferedReader.readLine();
//                Integer a=0;
//                                     while((lineTxt = bufferedReader.readLine()) != null){
//                                         String[] D = lineTxt.split(" ");
//                                         Disease d;
//                                         if(D[2].equals("\\N")){
//                                             d = new Disease(a++,D[0], DiseaseCategory.valueOf(D[1]),null);
//                                         }else {
//                                             d = new Disease(a++,D[0], DiseaseCategory.valueOf(D[1]),D[2]);
//                                         }
//                                             disease.add(d);
//                                         }
//            } else{
//                         System.out.println("找不到指定的文件");
//                     }
//                 } catch (Exception e) {
//                     System.out.println("读取文件内容出错");
//                     e.printStackTrace();
//                 }
//        System.out.println(disease);
//        return disease;
//
////        从数据库中读取疾病列表
////        return diseaseService.list();
//
//    }

//    @GetMapping("/getFactor/{tableName}")
//    public List<Factor> getFactor(@PathVariable String tableName){
//        List<Factor> factor = new ArrayList<>();
//        try{
//            String encoding = "UTF-8";
//            String filePath = path1+tableName+"_factor.txt";
//            File file = new File(filePath);
//            if(file.isFile()&&file.exists()){
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file),encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                lineTxt = bufferedReader.readLine();
//                Integer a = 0 ;
//                while((lineTxt = bufferedReader.readLine()) != null){
//                    String[] F = lineTxt.split(" ");
//                    Factor f;
//                        f = new Factor(a++,F[0], FactorCategory.valueOf(F[1]));
//                    factor.add(f);
//                }
//
//            } else{
//                System.out.println("找不到指定的文件");
//            }
//        } catch (Exception e) {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
//        System.out.println(factor);
//        return factor;
//
//
////        return factorService.list();
//    }
//    @GetMapping("/getAssociation/{tableName}")
//    public List<Association> getAssociation(@PathVariable String tableName) {
//        List<Association> associations = new ArrayList<>();
//        try{
//            String encoding = "UTF-8";
//            String filePath = path1+tableName+".txt";
//            File file = new File(filePath);
//            if(file.isFile()&&file.exists()){
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file),encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                lineTxt = bufferedReader.readLine();
//                Integer b=0;
//                Float w=1.0F;
//                while((lineTxt = bufferedReader.readLine()) != null){
//                    String[] A = lineTxt.split(" ");
//                    Association a;
//                    a = new Association(b,Integer.parseInt(A[0]),Integer.parseInt(A[1]),false,w);
//                    associations.add(a);
//                }
//            } else{
//                System.out.println("找不到指定的文件");
//            }
//        } catch (Exception e) {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
//        System.out.println(associations);
//        return associations;
////        return associationService.list();
//    }


    @PostMapping("/runing")
    public Graph runing(@RequestBody Mining mining) throws Exception{
        System.out.println(mining);
        return pythonRun(mining);
    }

    public Graph pythonRun(Mining mining) throws Exception{
        List<String> args = new LinkedList<>();
//        Tables table = tablesService.lambdaQuery()
//                .eq(Tables::getName,mining.getTableName())
//                .list().get(0);
        args.add("--disease="+mining.getDisease());
        args.add("--factor="+mining.getFactor());
        args.add("--task-type="+mining.getType());
        args.add("--table-name="+mining.getTableName());
//        args.add("--file-path="+path1);
        args.add("--database-host="+host);
        args.add("--database-password="+ password);
        args.add("--database-user="+ user);
        args.add("--database-dbname="+ dbname);
        args.add("--database-port="+ port);
//        mining.setTime((Timestamp) new Date());
//        System.out.println(mining.getTime());
//        Algorithm algorithm = algorithmService.lambdaQuery()
//                .eq(Algorithm::getAlgorithmName,mining.getAlgorithmName())
//                .list().get(0);
//        String relation = pythonRun.run(algorithm.getDeployFilePath(),args);
        String relation = pythonRun.run(path2+mining.getAlgorithmName()+"\\"+mining.getAlgorithmName()+".py",args);

        String[] disease = mining.getDisease().split(",");
        String[] factor = mining.getFactor().split(",");
        Graph graph = getGraph(relation,disease,factor,mining.getType());
        return graph;
    }
    public Graph getGraph(String relation,String[] disease,String[] factor,TaskType type) throws JSONException {
        Integer lenD = disease.length;
        Integer lenF = factor.length;
        List<Nodes> nodes = new ArrayList<>();

        List<Links> links = new ArrayList<>();

        List<Categories> categories = new ArrayList<>();

        Graph graph = new Graph();
        if(type.equals(TaskType.disease_mining)){
//            设置节点
            for(int i=0;i<lenD;i++){
                Nodes node = new Nodes();
                node.setId(i);
                node.setName(disease[i]);
                String cat = String.valueOf(diseaseService.lambdaQuery()
                        .eq(Disease::getName,disease[i])
                        .list().get(0).getCategory());
                DiseaseCategory d = DiseaseCategory.valueOf(cat);
                node.setCategory(d.ordinal());
                nodes.add(node);
            }
//            设置关联边
            JSONArray jsonArray = new JSONArray(relation);
            for(int i=0;i<jsonArray.length();i++){
                System.out.println(jsonArray.getString(i));
                Links link = new Links();
                String s = jsonArray.getString(i).substring(1,jsonArray.getString(i).length()-1);
                String[] a = s.split(",");
                link.setSource(a[0]);
                link.setTarget(a[1]);
                link.setValue(a[2]);
                links.add(link);
            }
            DiseaseCategory[] diseaseCategory = DiseaseCategory.values();
//            设置类型
            for(DiseaseCategory e:diseaseCategory){
                Categories category = new Categories();
                category.setName(e.toString());
                categories.add(category);
            }

            graph.setNodes(nodes);
            graph.setLinks(links);
            graph.setCategories(categories);
        }else if(type.equals(TaskType.factor_mining)){
//            设置节点
        for(int j=0;j<lenF;j++){
            Nodes node = new Nodes();
            node.setId(j);
            node.setName(factor[j]);
            String cat = String.valueOf(factorService.lambdaQuery()
                    .eq(Factor::getName,factor[j])
                    .list().get(0).getCategory());
            FactorCategory f = FactorCategory.valueOf(cat);
            node.setCategory(f.ordinal());
            nodes.add(node);
        }
//            设置关联边
            JSONArray jsonArray = new JSONArray(relation);
            for(int i=0;i<jsonArray.length();i++){
                Links link = new Links();
                String s = jsonArray.getString(i).substring(1,jsonArray.getString(i).length()-1);
                String[] a = s.split(",");
                link.setSource(a[0]);
                link.setTarget(a[1]);
                link.setValue(a[2]);
                links.add(link);
            }
            FactorCategory[] factorCategories = FactorCategory.values();
//            设置类型
            for(FactorCategory e:factorCategories){
                Categories category = new Categories();
                category.setName(e.toString());
                categories.add(category);
            }

            graph.setNodes(nodes);
            graph.setLinks(links);
            graph.setCategories(categories);
        }else if(type.equals(TaskType.disease_factor_mining)){
            //            设置节点
            int i;
            for(i=0;i<lenD;i++){
                Nodes node = new Nodes();
                node.setId(i);
                node.setName(disease[i]);
                String cat = String.valueOf(diseaseService.lambdaQuery()
                        .eq(Disease::getName,disease[i])
                        .list().get(0).getCategory());
                DiseaseCategory d = DiseaseCategory.valueOf(cat);
                node.setCategory(d.ordinal());
                nodes.add(node);
            }
            int j;
            for(j=i;j<lenF+i;j++){
                Nodes node = new Nodes();
                node.setId(j);
                node.setName(factor[j-i]);
                String cat = String.valueOf(factorService.lambdaQuery()
                        .eq(Factor::getName,factor[j-i])
                        .list().get(0).getCategory());
                FactorCategory f = FactorCategory.valueOf(cat);
                node.setCategory(f.ordinal()+6);
                nodes.add(node);
            }
//            设置关联边
            JSONArray jsonArray = new JSONArray(relation);
            for(i=0;i<jsonArray.length();i++){
                Links link = new Links();
                String s = jsonArray.getString(i).substring(1,jsonArray.getString(i).length()-1);
                String[] a = s.split(",");
                link.setSource(a[0]);
                link.setTarget(a[1]);
                link.setValue(a[2]);
                link.setIsNew(Boolean.parseBoolean(a[3]));
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
        }
        System.out.println(graph);

        return graph;
    }
//python执行得到的
}
