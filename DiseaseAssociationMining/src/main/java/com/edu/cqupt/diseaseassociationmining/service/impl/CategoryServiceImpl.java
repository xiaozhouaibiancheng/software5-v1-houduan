package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.CategoryEntity;
import com.edu.cqupt.diseaseassociationmining.mapper.CategoryMapper;
import com.edu.cqupt.diseaseassociationmining.service.CategoryService;
import com.edu.cqupt.diseaseassociationmining.vo.AddDiseaseVo;
import com.edu.cqupt.diseaseassociationmining.vo.UpdateDiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edu.cqupt.diseaseassociationmining.entity.CategoryEntity.*;

// TODO 公共模块新增类

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,CategoryEntity>
        implements CategoryService {

    @Autowired
    CategoryMapper dataManagerMapper;
    @Autowired
    CategoryMapper categoryMapper;
//    @Override
//    public List<CategoryEntity> getCategory() {
//
//        // 获取所有目录行程树形结构
//        List<CategoryEntity> categoryEntities = dataManagerMapper.selectList(null);
//        // 获取所有级结构
//        List<CategoryEntity> treeData = categoryEntities.stream().filter((categoryEntity) -> {
//            return categoryEntity.getParentId().equals("0") && categoryEntity.getIsDelete()==0;
//        }).map((level1Cat) -> {
//            level1Cat.setChildren(getCatChildren(level1Cat, categoryEntities));;
//            return level1Cat;
//        }).collect(Collectors.toList());
//
//        return treeData;
//    }
//@Override
//public List<CategoryEntity> getCategory(String uid) {
//    List<CategoryEntity> categoryEntities_private = new ArrayList<CategoryEntity>();
//    List<CategoryEntity> categoryEntities_share = new ArrayList<CategoryEntity>();
//    List<CategoryEntity> categoryEntities_common = new ArrayList<CategoryEntity>();
//    // 获取所有目录行程树形结构
//    List<CategoryEntity> categoryEntities = categoryMapper.selectList(null);
//    // 获取所有级结构
//    List<CategoryEntity> treeData = categoryEntities.stream().filter((categoryEntity) -> {
//        return categoryEntity.getParentId().equals("0") && categoryEntity.getIsDelete()==0;
//    }).map((level1Cat) -> {
//        level1Cat.setChildren(getCatChildren(level1Cat, categoryEntities));
//        return level1Cat;
//    }).collect(Collectors.toList());
//
//
//    CategoryEntity copiedTree1 = copyPrivareTreeStructure(treeData.get(0),uid);
//    copiedTree1.setLabel("私有数据集");
//    CategoryEntity copiedTree2 = copyShareTreeStructure(treeData.get(0));
//    copiedTree2.setLabel("共享数据集");
//    CategoryEntity copiedTree3 = copyCommonTreeStructure(treeData.get(0));
//    copiedTree3.setLabel("公共数据集");
//    List<CategoryEntity> res = new ArrayList<CategoryEntity>();
//    res.add(copiedTree1);
//    res.add(copiedTree2);
//    res.add(copiedTree3);
//    return res;
//}
    @Override
    public List<CategoryEntity> getCategory(String uid) {
        // 获取所有目录行程树形结构
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(null);
        // 获取所有级结构
        List<CategoryEntity> treeData = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentId().equals("0") && categoryEntity.getIsDelete() == 0;
        }).map((level1Cat) -> {
            level1Cat.setChildren(getCatChildren(level1Cat, categoryEntities));
            return level1Cat;
        }).collect(Collectors.toList());

        CategoryEntity copiedTree1 = copyPrivareTreeStructure(treeData.get(0), uid);
        copiedTree1.setLabel("私有数据集");
        CategoryEntity copiedTree2 = copyShareTreeStructure(treeData.get(0), uid);
        copiedTree2.setLabel("共享数据集");
        CategoryEntity copiedTree3 = copyCommonTreeStructure(treeData.get(0));
        copiedTree3.setLabel("公共数据集");
        List<CategoryEntity> res = new ArrayList<CategoryEntity>();
        res.add(copiedTree1);
        res.add(copiedTree2);
        res.add(copiedTree3);
        return res;
    }

    @Override
    public void removeNode(String id, String label) {
        categoryMapper.removeNode(id);
        categoryMapper.removeTable(label);
    }
    @Override
    public void removeNode(String id) {
        categoryMapper.removeNode(id);
    }

    // 获取1级目录下的所有子结构
    private List<CategoryEntity> getCatChildren(CategoryEntity level1Cat, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> children = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentId().equals(level1Cat.getId()) && categoryEntity.getIsDelete()==0; // 获取当前分类的所有子分类
        }).map((child) -> {
            // 递归设置子分类的所有子分类
            child.setChildren(getCatChildren(child, categoryEntities));
            return child;
        }).collect(Collectors.toList());
        return children;
    }
    @Override
    public void changeStatus(CategoryEntity categoryEntity) {
        System.out.println(categoryEntity.getStatus());
        if (categoryEntity.getStatus().equals("0")){
            categoryMapper.changeStatusToShare(categoryEntity.getId());
        }
        else if(categoryEntity.getStatus().equals("1")){
            categoryMapper.changeStatusToPrivate(categoryEntity.getId());
        }

    }
    @Override
    public List<CategoryEntity> getTaskCategory() {

        // 获取所有目录行程树形结构
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(null);
        // 获取所有级结构
        List<CategoryEntity> treeData = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentId().equals("0") && categoryEntity.getIsDelete()==0;
        }).map((level1Cat) -> {
            level1Cat.setChildren(getCatChildren(level1Cat, categoryEntities));
            return level1Cat;
        }).collect(Collectors.toList());

        return treeData;
    }

    @Override
    public List<CategoryEntity> getSpDisease() {
        List<CategoryEntity> res = categoryMapper.getSpDisease();
        return res;
    }

    @Override
    public List<CategoryEntity> getComDisease() {
        List<CategoryEntity> res = categoryMapper.getComDisease();
        return res;
    }

    @Override
    public String getLabelByPid(String pid) {
        return categoryMapper.getLabelByPid(pid);
    }

//    @Override
//    public void addParentDisease(String diseaseName) {
//        CategoryEntity categoryEntity = new CategoryEntity(null, 1, diseaseName, "0", 0, 0, null, null,null,null,null,null);
//        categoryMapper.insert(categoryEntity);
//    }


    /**
     * 下面几个模块可以直接覆盖一下
     * @return
     */
    //添加疾病管理模块

    /**
     * 修改
     * @return
     */
    @Override
    public List<CategoryEntity> getAllDisease(){
        // 获取所有目录行程树形结构
        List<CategoryEntity> categoryEntities = dataManagerMapper.selectList(null);
        // 获取所有级结构
        List<CategoryEntity> treeData = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentId().equals("1") && categoryEntity.getIsDelete()==0 && categoryEntity.getStatus()==null;
        }).map((level1Cat) -> {
            Map<String,Object> resultMap = getDiseaseChildren(level1Cat,categoryEntities);
//            Pair<List<CategoryEntity>,int[]> pair = getDiseaseChildren(level1Cat,categoryEntities);
            level1Cat.setChildren((List<CategoryEntity>) resultMap.get("categoryEntities"));
            //计算第二层目录下表数量和加上第二层表数量作为第一层的num
            int[] numsCat = (int[]) resultMap.get("intArray");
            int[] numsTab = getFileNums(level1Cat.getId());
            level1Cat.setTableNum0(numsCat[0]+numsTab[0]);
            level1Cat.setTableNum1(numsCat[1]+numsTab[1]);
            level1Cat.setTableNum2(numsCat[2]+numsTab[2]);
            return level1Cat;
        }).collect(Collectors.toList());

        return treeData;
    }

    /**
     * 新增
     */
//    private Pair<List<CategoryEntity>,int[]> getDiseaseChildren(CategoryEntity level1Cat, List<CategoryEntity> categoryEntities)
    private Map<String,Object> getDiseaseChildren(CategoryEntity level1Cat, List<CategoryEntity> categoryEntities)
    {
        List<CategoryEntity> children = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentId().equals(level1Cat.getId()) && categoryEntity.getIsDelete()==0 && categoryEntity.getStatus()==null; // 获取当前分类的所有子分类
        }).map((child) -> {
            // 递归设置子分类的所有子分类
//            child.setChildren(getCatChildren(child, categoryEntities));
//            Pair<List<CategoryEntity>,int[]> pair = getDiseaseChildren(child, categoryEntities);
            Map<String,Object> resultMap = getDiseaseChildren(child,categoryEntities);
            child.setChildren((List<CategoryEntity>) resultMap.get("categoryEntities"));
            //计算第三层目录下表数量和加上第三层表数量作为第二层的num
            int[] numsCat = (int[]) resultMap.get("intArray");
            int[] numsTab = getFileNums(child.getId());
            child.setTableNum0(numsCat[0]+numsTab[0]);
            child.setTableNum1(numsCat[1]+numsTab[1]);
            child.setTableNum2(numsCat[2]+numsTab[2]);

            return child;
        }).collect(Collectors.toList());
        int[] nums = new int[3];
        for(CategoryEntity category:children){
            nums[0] += category.getTableNum0();
            nums[1] += category.getTableNum1();
            nums[2] += category.getTableNum2();
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("categoryEntities",children);
        resultMap.put("intArray",nums);
//        return new Pair<>(children,nums);
        return resultMap;
    }
    // 获取第二层目录
//    private Pair<List<CategoryEntity>,int[]> getSecondLevelChildren(String parentId) {
//        // 获取所有第二层目录
//        List<CategoryEntity> secondLevelCategories = dataManagerMapper.selectList(null).stream()
//                .filter(categoryEntity -> categoryEntity.getParentId().equals(parentId) && categoryEntity.getIsDelete() == 0 && categoryEntity.getStatus()==null)
//                .map(level2Cat -> {
//                    Pair<List<CategoryEntity>,int[]> pair = getThirdLevelChildren((level2Cat.getId()));
//                    level2Cat.setChildren(pair.getKey());
//                    //计算第三层目录下表数量和加上第三层表数量作为第二层的num
//                    int[] numsCat = pair.getValue();
//                    int[] numsTab = getFileNums(level2Cat.getId());
//                    level2Cat.setTableNum0(numsCat[0]+numsTab[0]);
//                    level2Cat.setTableNum1(numsCat[1]+numsTab[1]);
//                    level2Cat.setTableNum2(numsCat[2]+numsTab[2]);
//
//                    return level2Cat;
//                })
//                .collect(Collectors.toList());
//        //遍历该父节点下所有目录，获取该节点下表的总和
//        int[] nums = new int[3];
//        for(CategoryEntity category:secondLevelCategories){
//                nums[0] += category.getTableNum0();
//                nums[1] += category.getTableNum1();
//                nums[2] += category.getTableNum2();
//        }
//        return new Pair<>(secondLevelCategories,nums);
//    }
    //获取第三层目录
//    private Pair<List<CategoryEntity>,int[]> getThirdLevelChildren(String parentId){
//        List<CategoryEntity> thirdLevelCategories = dataManagerMapper.selectList(null).stream()
//                .filter(categoryEntity -> categoryEntity.getParentId().equals(parentId) && categoryEntity.getIsDelete() == 0 && categoryEntity.getStatus()==null)
//                .map(level3Cat -> {
//                    //计算第四层表数量作为第三层的num
//                    int[] numsTab = getFileNums(level3Cat.getId());
//                    level3Cat.setTableNum0(numsTab[0]);
//                    level3Cat.setTableNum1(numsTab[1]);
//                    level3Cat.setTableNum2(numsTab[2]);
//
//                    level3Cat.setChildren(new ArrayList<>()); // 第二层目录没有子目录
//                    return level3Cat;
//                })
//                .collect(Collectors.toList());
//        //遍历该父节点下所有目录，获取该节点下表的总和
//        int[] nums = new int[3];
//        for(CategoryEntity category:thirdLevelCategories){
//            nums[0] += category.getTableNum0();
//            nums[1] += category.getTableNum1();
//            nums[2] += category.getTableNum2();
//        }
//        return new Pair<>(thirdLevelCategories,nums);
//    }
    private int[] getFileNums(String id){
        int[] nums = new int[3];
        List<CategoryEntity> secondLevelCategories = dataManagerMapper.selectList(null);
        for(CategoryEntity category:secondLevelCategories){
            if(category.getParentId().equals(id) && category.getIsDelete() == 0 && category.getStatus()!=null) {
                if (category.getStatus().equals("0"))
                    nums[0]++;
                else if (category.getStatus().equals("1"))
                    nums[1]++;
                else if (category.getStatus().equals("2"))
                    nums[2]++;
            }
        }
        return nums;
    }

    //添加一级病种
    @Override
    public int addCategory(AddDiseaseVo addDiseaseVo){
        CategoryEntity category = new CategoryEntity(null,addDiseaseVo.getCatLevel(),addDiseaseVo.getFirstDisease(),addDiseaseVo.getParentId(),0,0,addDiseaseVo.getUid(),null,addDiseaseVo.getUsername(),null,null,addDiseaseVo.getIcdCode(),null,null,0,0,0);
        return categoryMapper.insert(category);
    }

    @Override
    public Result updateCategory(UpdateDiseaseVo updateDiseaseVo){
            UpdateWrapper<CategoryEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", updateDiseaseVo.getCategoryId())
                    .set("label", updateDiseaseVo.getDiseaseName())
                    .set("parent_id", updateDiseaseVo.getParentId())
                    .set("uid", updateDiseaseVo.getUid())
                    .set("username", updateDiseaseVo.getUsername())
                    .set("icd_code",updateDiseaseVo.getIcdCode());
            return categoryMapper.update(null, updateWrapper)>0?Result.success("插入成功"):Result.fail("插入失败");
    }
    @Override
    public void removeCategorys(List<String> deleteIds){
        for(String id:deleteIds){
            UpdateWrapper<CategoryEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id)
                    .set("is_delete", 1);
            categoryMapper.update(null, updateWrapper);
        }
    }
    //添加病种
//    @Override
//    public int addCategory(CategoryEntity category) {
//        return categoryMapper.insert(category);
//    }

    /**
     * 删除下面添加病种模块
     * @param
     * @return
     */
    //添加病种
//    @Override
//    public Result addCategory(AddDiseaseVo addDiseaseVo){
//        Result result;
//        System.out.println(addDiseaseVo);
//        System.out.println(addDiseaseVo.getSecondDisease());
//        if(addDiseaseVo.getSecondDisease().equals("")){
//            result = addFirstDisease(addDiseaseVo);
//        }else{
//            result = addSecondDisease(addDiseaseVo);
//        }
//        return result;
//    }
//    private Result addFirstDisease(AddDiseaseVo addDiseaseVo){
//        //判断该病种是否已经存在
//        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("parent_id", "1")
//                .eq("label", addDiseaseVo.getFirstDisease())
//                .eq("is_delete",0);
//        CategoryEntity category = categoryMapper.selectOne(queryWrapper);
//        if(category==null){
//            CategoryEntity categoryEntity = new CategoryEntity(null, 2, addDiseaseVo.getFirstDisease(), "1", 0, 0, addDiseaseVo.getUid(), null, addDiseaseVo.getUsername(), null,null,null,0,0,0);
//            categoryMapper.insert(categoryEntity);
//            return Result.success("添加成功");
//        }else{
//            return Result.fail("该一级病种已经存在");
//        }
//    }
//    private Result addSecondDisease(AddDiseaseVo addDiseaseVo){
//        //获取一级病种，若不存在则插入,并取得一级病种id
//        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("parent_id", "1")
//                .eq("label", addDiseaseVo.getFirstDisease())
//                .eq("is_delete",0);
//        CategoryEntity category = categoryMapper.selectOne(queryWrapper);
//        String categoryId;
//        if(category==null){
//            System.out.println("该一级目录不存在，已添加");
//            CategoryEntity categoryEntity = new CategoryEntity(null, 2, addDiseaseVo.getFirstDisease(), "1", 0, 0, addDiseaseVo.getUid(), null, addDiseaseVo.getUsername(),null,null, null,0,0,0);
//            categoryMapper.insert(categoryEntity);
//            QueryWrapper<CategoryEntity> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("parent_id", "1")
//                    .eq("label", addDiseaseVo.getFirstDisease())
//                    .eq("is_delete",0);
//            CategoryEntity category1 = categoryMapper.selectOne(queryWrapper1);
//            categoryId = category1.getId();
//        }else{
//            System.out.println("该一级目录存在");
//            categoryId = category.getId();
//        }
//        //获取二级病种，若不存在则插入
//        QueryWrapper<CategoryEntity> queryWrapper2 = new QueryWrapper<>();
//        queryWrapper2.eq("parent_id", categoryId)
//                .eq("label", addDiseaseVo.getSecondDisease())
//                .eq("is_delete",0);
//        CategoryEntity category2 = categoryMapper.selectOne(queryWrapper2);
//        System.out.println(category2);
//        if(category2==null){
//            CategoryEntity categoryEntity2 = new CategoryEntity(null, 3, addDiseaseVo.getSecondDisease(), categoryId, 0, 0, addDiseaseVo.getUid(), null, addDiseaseVo.getUsername(), null,null,null,0,0,0);
//            categoryMapper.insert(categoryEntity2);
//            return Result.success("添加成功");
//        }else{
//            return Result.fail("该二级病种已经存在");
//        }
//    }

    //    下面方法是管理员端-数据管理新增
    @Override
    public List<CategoryEntity> getLevel2Label() {
        return categoryMapper.getLevel2Label();
    }

    @Override
    public List<CategoryEntity> getLabelsByPid(String pid) {
        return categoryMapper.getLabelsByPid(pid);
    }
    public void updateTableNameByTableName(String oldTableName, String tableName, String tableStatus) {
        System.out.println("status: " + tableStatus);
        categoryMapper.updateTableNameByTableName(oldTableName, tableName, tableStatus);
    }


}
