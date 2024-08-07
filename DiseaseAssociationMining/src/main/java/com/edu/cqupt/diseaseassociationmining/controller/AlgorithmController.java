package com.edu.cqupt.diseaseassociationmining.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.Algorithm;
import com.edu.cqupt.diseaseassociationmining.service.AlgorithmService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {
    @Resource
    AlgorithmService algorithmService;
    @Value("${gorit.file.root.path2}")
    private String filePath;

    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String algorithmType,
                               @RequestParam String algorithmName){
        QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<Algorithm>();
        queryWrapper.like(StringUtils.isNotBlank(algorithmType),"task_type",algorithmType);
        queryWrapper.like(StringUtils.isNotBlank(algorithmName), "algorithm_name", "%" + algorithmName + "%");
        PageInfo<Algorithm> pageInfo = algorithmService.findByPageService(pageNum, pageSize,queryWrapper);
        return Result.success(pageInfo);
    }

    @PostMapping("/list")
    public Object index(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize){
        System.out.println("pageNum:"+pageNum);
        System.out.println("pageSize:"+pageSize);

        Page<Algorithm> pg = new Page<>(pageNum,pageSize);
        IPage<Algorithm> algorithmIPage = algorithmService.page(pg);
        System.out.println("总条数 ------> [{}]"+algorithmIPage.getTotal());
        System.out.println("当前页数 ------> [{}]" + algorithmIPage.getCurrent());
        System.out.println("当前每页显示数 ------> [{}]" + algorithmIPage.getSize());
        System.out.println("当前页数据 ------> [{}]" + algorithmIPage.getRecords());

        List<Algorithm> list = algorithmService.list();
        return algorithmIPage;
    }
    @PostMapping("/update")
    public Boolean algorithmUpdate(@RequestBody Algorithm algorithm) throws IOException {
//        String fileName = algorithmService.getById(algorithm.getId()).getAlgorithmName();
//        if(!fileName.equals(algorithm.getAlgorithmName())){
//            File oldName = new File(filePath+fileName);
//            File newName = new File(filePath+algorithm.getAlgorithmName());
//            if (newName.exists()) {  //  确保新的文件名不存在
//                throw new java.io.IOException("file exists");
//            }
//            if(oldName.renameTo(newName)) {
//                System.out.println("已重命名");
//                algorithm.setDeployFilePath(filePath+algorithm.getAlgorithmName());
//            } else {
//                System.out.println("Error");
//            }
//        }
        return algorithmService.updateById(algorithm);
    }
    @PostMapping("/insert")
    public  Boolean algorithmInsert(@RequestBody Algorithm algorithm){
        algorithm.setDeployFilePath(filePath + algorithm.getAlgorithmName()+"/"+algorithm.getAlgorithmName()+".py");
        return algorithmService.save(algorithm);
    }
    @GetMapping("/getById/{algorithmId}")
    public Algorithm getById(@PathVariable int algorithmId){
        return algorithmService.getById(algorithmId);
    }

    @PostMapping("/getAll")
    public List<Algorithm> getAll(){
        return algorithmService.list();
    }
    @GetMapping("/getByName/{algorithmName}")
    public List<Algorithm> getByName(@PathVariable String algorithmName){
        List<Algorithm> algorithm = algorithmService.lambdaQuery()
                .eq(Algorithm::getAlgorithmName,algorithmName)
                .list();
        return algorithm;
    }
    @GetMapping("/getByType/{algorithmType}")
    public List<Algorithm> getByType(@PathVariable String algorithmType){
        List<Algorithm> algorithm = algorithmService.lambdaQuery()
                .eq(Algorithm::getTaskType,algorithmType)
                .list();
        return algorithm;
    }
    @GetMapping("/delete/{algorithmId}")
    public Boolean algorithmDelete(@PathVariable int algorithmId){
        String fileName = algorithmService.getById(algorithmId).getAlgorithmName();
        try{
            File file = new File(filePath+fileName);
            if(file.delete()){
                System.out.println(file.getName() + " 文件已被删除！");
            }else{
                System.out.println("文件删除失败！");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        QueryWrapper<Algorithm> queryWrapper  =new QueryWrapper<>();
        queryWrapper.eq("id",algorithmId);
        return algorithmService.remove(queryWrapper);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

    // 日志打印
    private Logger log = LoggerFactory.getLogger("FileController");

    // 文件上传 （可以多文件上传）
    @PostMapping("/upload")
    public Result fileUploads(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        // 得到格式化后的日期
//        String format = sdf.format(new Date());
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        // 时间 和 日期拼接
        String newFileName = fileName;
        // 得到文件保存的位置以及新文件名
        File dest = new File(filePath + newFileName+"/"+newFileName+".py");
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            // 打印日志
            log.info("上传成功，当前上传的文件保存在 {}",filePath + newFileName);
            // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。
            return Result.success("上传成功");
        } catch (IOException e) {
            log.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        return Result.fail("上传错误");
    }

}
