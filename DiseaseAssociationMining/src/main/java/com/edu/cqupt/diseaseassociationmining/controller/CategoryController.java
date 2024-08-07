package com.edu.cqupt.diseaseassociationmining.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.*;
import com.edu.cqupt.diseaseassociationmining.mapper.CategoryMapper;
import com.edu.cqupt.diseaseassociationmining.mapper.TableDescribeMapper;
import com.edu.cqupt.diseaseassociationmining.mapper.UserMapper;
import com.edu.cqupt.diseaseassociationmining.service.Category2Service;
import com.edu.cqupt.diseaseassociationmining.service.CategoryService;
import com.edu.cqupt.diseaseassociationmining.service.UserLogService;
import com.edu.cqupt.diseaseassociationmining.vo.AddDiseaseVo;
import com.edu.cqupt.diseaseassociationmining.vo.DeleteDiseaseVo;
import com.edu.cqupt.diseaseassociationmining.vo.UpdateDiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

// TODO 公共模块新增类
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    Category2Service category2Service;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    TableDescribeMapper tableDescribeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserLogService userLogService;

    // 获取目录
    @GetMapping("/category")
    public Result<List<CategoryEntity>> getCatgory(@RequestParam String uid){
        List<CategoryEntity> list = categoryService.getCategory(uid);
//        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    @GetMapping("/getTableNumber")
    public Result getTableNumber(){ // 参数表的Id
        QueryWrapper<TableDescribeEntity> queryWrapper = new QueryWrapper<>();

        int count = Math.toIntExact(tableDescribeMapper.selectCount(queryWrapper));

        return Result.success("200",JSON.toJSONString(count));
    }
    @GetMapping("/Taskcategory")
    public Result<List<CategoryEntity>> getCatgory(){
        List<CategoryEntity> list = categoryService.getTaskCategory();
//        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    // 字段管理获取字段
    @GetMapping("/category2")
    public Result<List<Category2Entity>> getCatgory2(){
        List<Category2Entity> list = category2Service.getCategory2();
        return Result.success("200",list);
    }


    // 创建一种新的疾病
    @PostMapping("/addDisease")
    public Result addDisease(@RequestBody CategoryEntity categoryNode){
        System.out.println("参数为："+ JSON.toJSONString(categoryNode));
        categoryService.save(categoryNode);
        return Result.success(200,"新增目录成功");
    }

    // 删除一个目录
    @Transactional
    @GetMapping("/category/remove")
    public Result removeCate(CategoryEntity categoryEntity){
        System.out.println("要删除的目录为："+JSON.toJSONString(categoryEntity));
        if(categoryEntity.getIsLeafs()==0){
            categoryService.removeNode(categoryEntity.getId());
        }
        else {
            categoryService.removeNode(categoryEntity.getId(),categoryEntity.getLabel());
            TableDescribeEntity tableDescribeEntity = tableDescribeMapper.selectOne(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
            if(tableDescribeEntity.getTableSize()!=0){
                userMapper.recoveryUpdateUserColumnById(Integer.parseInt(tableDescribeEntity.getUid()),new Double(tableDescribeEntity.getTableSize()));
            }
            tableDescribeMapper.delete(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
//            tTableMapper.delete(new QueryWrapper<tTable>().eq("table_name",categoryEntity.getLabel()));

        }
        return Result.success(200,"删除成功");
    }
    @GetMapping("/changeStatus")
    public Result changeStatus(CategoryEntity categoryEntity){
        categoryService.changeStatus(categoryEntity);
        return Result.success(200,"修改成功",null);
    }

//    @GetMapping("/addParentDisease")
//    public Result addParentDisease(@RequestParam("diseaseName") String diseaseName){
//        System.out.println("name:"+diseaseName);
//        categoryService.addParentDisease(diseaseName);
//        return Result.success(200,"添加成功");
//    }


    /**
     *
     * @return
     */
//    zongqing新增疾病管理模块
    @GetMapping("/category/getAllDisease")
    public Result<List<CategoryEntity>> getAllDisease(){
        List<CategoryEntity> list = categoryService.getAllDisease();
        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    /**
     * 修改。加了isNULL条件
     */
    @GetMapping("/category/checkDiseaseName/{diseaseName}")
    public Result checkDiseaseName(@PathVariable String diseaseName){
        QueryWrapper<CategoryEntity> queryWrapper = Wrappers.query();
        queryWrapper.eq("label", diseaseName)
                .eq("is_delete", 0)
                .isNull("status");
        CategoryEntity category = categoryMapper.selectOne(queryWrapper);
        return category==null?Result.success("200","病种名可用"):Result.fail("400","病种名已存在");
    }

    /**
     * 修改
     * @param
     * @return
     */
    @PostMapping("/category/addCategory")
    public Result addCategory(@RequestBody AddDiseaseVo addDiseaseVo){
        if(categoryService.addCategory(addDiseaseVo)>0){
            UserLog userLog = new UserLog(null,Integer.parseInt(addDiseaseVo.getUid()),new Date(),"添加病种"+addDiseaseVo.getFirstDisease(),addDiseaseVo.getUsername());
            userLogService.save(userLog);
            return Result.success("添加病种成功");
        }else{
            UserLog userLog = new UserLog(null,Integer.parseInt(addDiseaseVo.getUid()),new Date(),"添加病种失败",addDiseaseVo.getUsername());
            userLogService.save(userLog);
            return Result.fail("添加病种失败");
        }
    }
    @PostMapping("/category/updateCategory")
    public Result updateCategory(@RequestBody UpdateDiseaseVo updateDiseaseVo){
        UserLog userLog = new UserLog(null,Integer.parseInt(updateDiseaseVo.getUid()),new Date(),"修改病种"+updateDiseaseVo.getOldName()+"为"+updateDiseaseVo.getDiseaseName(),updateDiseaseVo.getUsername());
        userLogService.save(userLog);
        return categoryService.updateCategory(updateDiseaseVo);
    }
    @PostMapping("/category/deleteCategory")
    public Result deleteCategory(@RequestBody DeleteDiseaseVo deleteDiseaseVo){
        StringJoiner joiner = new StringJoiner(",");
        for (String str : deleteDiseaseVo.getDeleteNames()) {
            joiner.add(str);
        }
        UserLog userLog = new UserLog(null,Integer.parseInt(deleteDiseaseVo.getUid()),new Date(),"删除病种："+joiner.toString(),deleteDiseaseVo.getUsername());
        userLogService.save(userLog);
        categoryService.removeCategorys(deleteDiseaseVo.getDeleteIds());
        return Result.success("删除成功");
    }

    // 新增可共享用户列表
    @PostMapping("/category/changeToShare")
    public Result changeToShare(@RequestBody Map<String, Object> requestData){
        String nodeid = (String) requestData.get("nodeid");
        String uid_list = (String) requestData.get("uid_list");
        CategoryEntity entity = new CategoryEntity();
        entity.setUidList(uid_list);
        entity.setStatus("1");
        UpdateWrapper<CategoryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", nodeid);
        int res = categoryMapper.update(entity, updateWrapper);
        if(res == 1){
            return Result.success(200,"修改成功");
        }
        else {
            return Result.fail(500,"修改失败");
        }
    }
    //新增可共享用户列表
    @PostMapping("/category/changeToPrivate")
    public Result changeToPrivate(@RequestBody Map<String, Object> requestData){
        String nodeid = (String) requestData.get("nodeid");
        CategoryEntity entity = new CategoryEntity();
        entity.setUidList("");
        entity.setStatus("0");
        UpdateWrapper<CategoryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", nodeid);
        int res = categoryMapper.update(entity, updateWrapper);
        if(res == 1){
            return Result.success(200,"修改成功");
        }
        else {
            return Result.fail(500,"修改失败");
        }
    }
    //新增可共享用户列表
    @PostMapping("/category/getNodeInfo")
    public Result getNodeInfo(@RequestBody Map<String, Object> requestData){
        String nodeid = (String) requestData.get("nodeid");
        String uid = (String) requestData.get("uid");

        QueryWrapper<CategoryEntity> queryWrapper = Wrappers.query();
        queryWrapper.eq("id",nodeid);
        CategoryEntity categoryEntity = categoryMapper.selectOne(queryWrapper);
        String includedUids = categoryEntity.getUidList();
        //使用 split() 方法返回的数组是一个固定长度的数组，无法修改其大小。
        //可以使用 Arrays.asList() 方法将数组转换为 ArrayList，然后再添加额外的元素。
        List<String> includedUidList = new ArrayList<>();
        if (includedUids != null && !includedUids.isEmpty()) {
            includedUidList = new ArrayList<>(Arrays.asList(includedUids.split(",")));
        }

        includedUidList.add(uid);
        List<Integer> includedUidIntList = includedUidList.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        System.out.println("includedUidIntList:"+includedUidIntList);

        QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
        userQueryWrapper1.notIn("uid", includedUidIntList);
        List<User> excludeUserList = userMapper.selectList(userQueryWrapper1);
        System.out.println("excludeUserList:"+excludeUserList);

        QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<>();
        includedUidIntList.remove(Integer.parseInt(uid));
        userQueryWrapper2.in("uid", includedUidIntList);
        List<User> includeUserList = userMapper.selectList(userQueryWrapper2);
        System.out.println("includeUserList:"+includeUserList);


        //
        List<String> tempRes = new ArrayList<>();
        List<Map<String, Object>> included = new ArrayList<>();
        for (User user : includeUserList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", String.valueOf(user.getUid()));
            resultMap.put("label", user.getUsername());
            tempRes.add(String.valueOf(user.getUid()));
            included.add(resultMap);
        }
        System.out.println("tempRes:"+tempRes);


        List<Map<String, Object>> excluded = new ArrayList<>();
        for (User user : excludeUserList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", String.valueOf(user.getUid()));
            resultMap.put("label", user.getUsername());
            excluded.add(resultMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("included", included);
        result.put("excluded", excluded);
        return  Result.success(200,"操作成功", tempRes);
    }

}

