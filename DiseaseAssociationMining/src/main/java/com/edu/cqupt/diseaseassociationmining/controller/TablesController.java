package com.edu.cqupt.diseaseassociationmining.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edu.cqupt.diseaseassociationmining.common.DiseaseCategory;
import com.edu.cqupt.diseaseassociationmining.common.FactorCategory;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.Disease;
import com.edu.cqupt.diseaseassociationmining.entity.Factor;
import com.edu.cqupt.diseaseassociationmining.entity.Tables;
import com.edu.cqupt.diseaseassociationmining.entity.UserLog;
import com.edu.cqupt.diseaseassociationmining.mapper.UserMapper;
import com.edu.cqupt.diseaseassociationmining.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


@RestController
@RequestMapping("/tables")
public class TablesController {
    @Resource
    TablesService tablesService;

    @Resource
    TableDataService tableDataService;
    @Resource
    UserLogService userLogService;
    @Resource
    UserMapper userMapper;
    @Resource
    TableDescribeService tableDescribeService;
    @Resource
    CategoryService categoryService;

    @Resource
    AssociationService associationService;
    @Resource
    DiseaseService diseaseService;
    @Resource
    FactorService factorService;
    @Value("${gorit.file.root.path1}")
    private String filePath;

//    获取数据
    @GetMapping("/getTableData")
    public Result getTableData(@RequestParam("tableId") String tableId, @RequestParam("tableName") String tableName){
        System.out.println("tableId=="+tableId+"   tableName=="+tableName);
        List<LinkedHashMap<String, Object>> tableData = tableDataService.getTableData(tableId, tableName);
        return Result.success("100",tableData);
    }

//    获取知识库中的内容
//    @PostMapping("/getKnowledgeBase")
//    public List<AssociationView> getKnowledgeBase(){
//        List<Disease> diseases = diseaseService.list();
//        List<Factor> factors = factorService.list();
//        List<Association> associations = associationService.list();
//        List<AssociationView> associationViews = new ArrayList<>();
//        Integer source,target;
//        for(Association a:associations){
//            AssociationView associationView = new AssociationView();
//            source = a.getSource();
//            target = a.getTarget();
//            for(Disease d:diseases){
//                if(d.getId().equals(source)){
//                    associationView.setSource(d.getName());
//                }
//            }
//            for(Factor f:factors){
//                if(f.getId().equals(target)){
//                    associationView.setTarget(f.getName());
//                }
//            }
//            associationViews.add(associationView);
//        }
//        return associationViews;
//    }

    @PostMapping("/update")
    public Boolean tablesUpdate(@RequestBody Tables tables) throws IOException {
        String fileName = tablesService.getById(tables.getId()).getName();
        if(!fileName.equals(tables.getName())){
            File oldName = new File(filePath+fileName);
            File newName = new File(filePath+tables.getName());
            if (newName.exists()) {  //  确保新的文件名不存在
                throw new java.io.IOException("file exists");
            }
            if(oldName.renameTo(newName)) {
                System.out.println("已重命名");
            } else {
                System.out.println("Error");
            }
        }
        return tablesService.updateById(tables);
    }
    @PostMapping("/insert")
    public  Boolean tableInsert(@RequestBody Tables tables){
        return tablesService.save(tables);
    }
    @GetMapping("/getById/{tablesId}")
    public Tables getById(@PathVariable int tablesId){
        return tablesService.getById(tablesId);
    }

    @PostMapping("/getAll")
    public List<Tables> getAll(){
        return tablesService.list();
    }
    @GetMapping("/getByName/{tablesName}")
    public List<Tables> getByName(@PathVariable String tablesName){
        List<Tables> tables = tablesService.lambdaQuery()
                .eq(Tables::getName,tablesName)
                .list();
        return tables;
    }
    @GetMapping("/delete")
    public boolean tablesDelete(
            @RequestParam("id") Integer id,
            @RequestParam("tableSize") float tableSize,
            @RequestParam("tableName") String tableName,
            @RequestParam("uid") Integer uid,
            @RequestParam("username") String username){
        tableDescribeService.deleteByTableName(tableName);
        UserLog userLog = new UserLog(null,uid,new Date(),"删除数据库中表："+tableName,username);
        userLogService.save(userLog);
        userMapper.addTableSize(uid, tableSize);
        userLog.setOpType("更新"+username+"可上传容量");
        userLogService.save(userLog);

        QueryWrapper<Tables> queryWrapper  =new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return tablesService.remove(queryWrapper);
    }
//    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

    // 日志打印
//    private Logger log = LoggerFactory.getLogger("FileController");

    // 文件上传 （可以多文件上传）
//    @PostMapping("/upload")
//    public Result fileUploads(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
//        // 得到格式化后的日期
////        String format = sdf.format(new Date());
//        // 获取上传的文件名称
//        String fileName = file.getOriginalFilename();
//        // 时间 和 日期拼接
////        String newFileName = format + "_" + fileName;
//        String newFileName = fileName;
//        // 得到文件保存的位置以及新文件名
//        File dest = new File(filePath + newFileName);
//        try {
//            // 上传的文件被保存了
//            file.transferTo(dest);
//            // 打印日志
//            log.info("上传成功，当前上传的文件保存在 {}",filePath + newFileName);
//            // 上传到首页统计数据
////            if(newFileName.endsWith("disease.txt")){
////                insertDisease(newFileName);
////            }else if(newFileName.endsWith("factor.txt")){
////                insertFactor(newFileName);
////            }else{
////                insertAssociation(newFileName);
////            }
//            // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。
//            return Result.success("上传成功");
//        } catch (IOException e) {
//            log.error(e.toString());
//        }
//        // 待完成 —— 文件类型校验工作
//        return Result.fail("上传错误");
//    }

    // 文件上传
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("tableName") String tableName,
                             @RequestParam("uid") Integer uid,
                             @RequestParam("createUser") String createUser,
                             @RequestParam("description") String description,
                             @RequestParam("tableSize") float tableSize
    ){
        // 保存表数据信息
        try {
            System.out.println("数据表大小："+tableSize);
            List<String> featureList = tableDataService.upload(file,tableName);
            Tables tables = new Tables(null,tableName,tableName,1,createUser,description,new Date(),tableSize,uid);
            tablesService.save(tables);
            UserLog userLog = new UserLog(null,uid,new Date(),"上传多病种任务表："+tableName,createUser);
            userLogService.save(userLog);
            userMapper.minusTableSize(uid, tableSize);
            userLog.setOpType("在user表中修改容量");
            userLogService.save(userLog);
            return Result.success("200",featureList); // 返回表头信息
        }catch (Exception e){
            e.printStackTrace();
            return Result.success(500,"文件上传异常");
        }
    }


    public void insertDisease(String fileName){
        try{
            String encoding = "UTF-8";
            String fileP = filePath+fileName;
            File file = new File(fileP);
            if(file.isFile()&&file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt=bufferedReader.readLine();
                Integer a =0;
                while((lineTxt= bufferedReader.readLine())!=null){
                    String[] D = lineTxt.split(" ");
                    List<Disease> diseases = diseaseService.lambdaQuery()
                            .eq(Disease::getName,D[0])
                            .list();
                    if(diseases.size()==0){
                        Disease d = new Disease();
                        d.setName(D[0]);
                        d.setCategory(DiseaseCategory.valueOf(D[1]));
                        if(D[2].equals("\\N")){
                            d.setCategory2(null);
                        }else {
                            d.setCategory2(D[2]);
                        }
                        diseaseService.save(d);
                    }
                }
            }
        }catch(Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
    public void insertFactor(String fileName){
        try{
            String encoding = "UTF-8";
            String fileP = filePath+fileName;
            File file = new File(fileP);
            if(file.isFile()&&file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt=bufferedReader.readLine();
                Integer a =0;
                while((lineTxt= bufferedReader.readLine())!=null){
                    String[] D = lineTxt.split(" ");
                    List<Factor> factors = factorService.lambdaQuery()
                            .eq(Factor::getName,D[0])
                            .list();
                    if(factors.size()==0){
                        Factor f = new Factor();
                        f.setName(D[0]);
                        f.setCategory(FactorCategory.valueOf(D[1]));
                        factorService.save(f);
                    }
                }
            }
        }catch(Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
    public void insertAssociation(String fileName){
//        try{
//            String encoding = "UTF-8";
//            String filePath = "F:\\java\\medical\\DiseaseAssociationMining\\src\\main\\resources\\file\\"+fileName;
//            File file = new File(filePath);
//            if(file.isFile()&&file.exists()){
//                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt=bufferedReader.readLine();
//                Integer a =0;
//                while((lineTxt= bufferedReader.readLine())!=null){
//                    String[] D = lineTxt.split(" ");
//                    List<Association> associations = associationService.lambdaQuery()
//                            .eq(Factor::getName,D[0])
//                            .list();
//                    if(factors.size()==0){
//                        Factor f = new Factor();
//                        f.setName(D[0]);
//                        f.setCategory(FactorCategory.valueOf(D[1]));
//                        factorService.save(f);
//                    }
//                }
//            }
//        }catch(Exception e) {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
    }
}
