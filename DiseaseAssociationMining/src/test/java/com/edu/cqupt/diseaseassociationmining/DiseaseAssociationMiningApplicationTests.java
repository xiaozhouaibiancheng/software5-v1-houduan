package com.edu.cqupt.diseaseassociationmining;

import com.edu.cqupt.diseaseassociationmining.mapper.AssociationMapper;
import com.edu.cqupt.diseaseassociationmining.service.AlgorithmService;
import com.edu.cqupt.diseaseassociationmining.service.DiseaseService;
import com.edu.cqupt.diseaseassociationmining.service.MiningService;
import com.edu.cqupt.diseaseassociationmining.tool.PythonRun;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@MapperScan("com.edu.cqupt.diseaseassociationmining.mapper")
class DiseaseAssociationMiningApplicationTests {
    @Autowired
    AssociationMapper associationMapper;
    @Autowired
    AlgorithmService algorithmService;
    @Autowired
    MiningService miningService;
    @Resource
    DataSourceProperties dataSourceProperties;
    @Resource
    PythonRun pythonRun;
    @Resource
    DiseaseService diseaseService;

//    @Test
//    void contextLoads() {
////        测试mapper
////        List<Association> associations = associationMapper.selectList(null);
////        associations.forEach(System.out::println);
////        测试service
////        Algorithm a = algorithmService.getById(1);
////        System.out.println(a);
////        QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
////        queryWrapper.eq("id",1);
////        Algorithm b = algorithmService.getOne(queryWrapper);
////        System.out.println(b);
//
//        int a = 18;
//        int b = 23;
//        try {
//            String[] args = new String[] { "python", "F:\\java\\medical\\DiseaseAssociationMining\\src\\main\\resources\\test.py"};
//            Process proc = Runtime.getRuntime().exec("python F:\\java\\medical\\DiseaseAssociationMining\\src\\main\\resources\\test1.py 1 2");// 执行py文件
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
//            System.out.println(111111);
//            proc.waitFor();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(dataSourceProperties.getUrl());
//
//    }
    @Test
    void test(){
//        List<Disease> disease = new ArrayList<>();
//        try{
//            String encoding = "UTF-8";
//            String tableName = "association";
//            String filePath = "F:\\java\\medical\\DiseaseAssociationMining\\src\\main\\resources\\file\\"+tableName+"_disease.txt";
//            File file = new File(filePath);
//            if(file.isFile()&&file.exists()){
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file),encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                lineTxt = bufferedReader.readLine();
//                while((lineTxt = bufferedReader.readLine()) != null){
//                    System.out.println(lineTxt);
//                    Disease d = new Disease(lineTxt);
//                    disease.add(d);
//                }
//            } else{
//                System.out.println("找不到指定的文件");
//            }
//        } catch (Exception e) {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
//        System.out.println(disease);

//        Integer a = diseaseService.count();
//        System.out.println(a);

    }
//    @Test
//    void testRun() throws  Exception{
//        List<String> args = new LinkedList<>();
////        Tables table = tablesService.lambdaQuery()
////                .eq(Tables::getName,mining.getTableName())
////                .list().get(0);
////        args.add("--disease="+mining.getDisease());
////        args.add("--factor="+mining.getFactor());
////        args.add("--task-type="+mining.getType());
////        args.add("--table-name="+mining.getTableName());
//        args.add("--database-password="+dataSourceProperties.getUrl());
//        args.add("--database-password="+ dataSourceProperties.getPassword());
//        args.add("--database-user="+ dataSourceProperties.getUsername());
//        String a =  pythonRun.run("F:\\java\\medical\\DiseaseAssociationMining\\src\\main\\resources\\algorithm\\SimRank0.py",args);
//        System.out.println(a.getClass().toString());
//        System.out.println(a);
//
////        String json = "[[0, 1, 2, 1.345234], [4, 5, 6, 7.342], [8, 9, 10, 11.6733]]";
////        JSONArray jsonArray = new JSONArray(json);
////        ArrayList<String> list=new ArrayList<>();
////        for(int i=0;i<jsonArray.length();i++){
////            //jArray.optString(i);//等价于getXXX
////            list.add(jsonArray.getString(i));
////            String s = jsonArray.getString(i).substring(1,jsonArray.getString(i).length()-1);
////            System.out.println(s);
////            String[] a = s.split(",");
////            System.out.println(a[0]);
////        }
////
////        System.out.println("解析结果："+list.get(1).split(","));
//    }

//    @Test
//    void loadTxt() throws Exception{
////                从文件中读取疾病列表
//        List<Disease> disease = new ArrayList<>();
//        try{
//            String encoding = "UTF-8";
//            String filePath = "F:\\java\\medical\\DiseaseAssociationMining\\src\\main\\resources\\file\\"+"association_disease.txt";
//            File file = new File(filePath);
//            if(file.isFile()&&file.exists()){
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file),encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                lineTxt = bufferedReader.readLine();
//                Integer a =0;
//                while((lineTxt = bufferedReader.readLine()) != null){
//                    String[] D = lineTxt.split(" ");
//                    Disease d;
//                    if(D[2].equals("\\N")){
//                        d = new Disease(a++,D[0], DiseaseCategory.valueOf(D[1]),null);
//                    }else {
//                        d = new Disease(a++,D[0], DiseaseCategory.valueOf(D[1]),D[2]);
//                    }
////                                             System.out.println(D[0]+D[1]+D[2]);
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
//    }

}
