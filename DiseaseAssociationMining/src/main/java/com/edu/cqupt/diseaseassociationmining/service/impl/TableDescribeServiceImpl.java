package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.cqupt.diseaseassociationmining.entity.*;
import com.edu.cqupt.diseaseassociationmining.mapper.CategoryMapper;
import com.edu.cqupt.diseaseassociationmining.mapper.TableDescribeMapper;
import com.edu.cqupt.diseaseassociationmining.mapper.UserMapper;
import com.edu.cqupt.diseaseassociationmining.service.TableDescribeService;
import com.edu.cqupt.diseaseassociationmining.service.UserLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// TODO 公共模块新增类

@Service
public class TableDescribeServiceImpl extends ServiceImpl<TableDescribeMapper, TableDescribeEntity> implements TableDescribeService {
    @Autowired
    private TableDescribeMapper tableDescribeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserLogService userLogService;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> storeTableData(MultipartFile file, String tableName) throws IOException {
        ArrayList<String> featureList = null;
        if (!file.isEmpty()) {
            // 使用 OpenCSV 解析 CSV 文件
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8"));
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> csvData = csvReader.readAll();
            csvReader.close();
            // 获取表头信息
            String[] headers = csvData.get(0);
            featureList = new ArrayList<String>(Arrays.asList(headers));
            System.out.println("表头信息为："+ JSON.toJSONString(headers));
            // 删除表头行，剩余的即为数据行
            csvData.remove(0);
            // 创建表信息
            tableDescribeMapper.createTable(headers,tableName);
            // 保存表头信息和表数据到数据库中
            for (String[] row : csvData) { // 以此保存每行信息到数据库中
                tableDescribeMapper.insertRow(row,tableName);
            }
        }
        return featureList;
    }
    @Override
    @Transactional
    public List<String> uploadDataTable(MultipartFile file, String pid, String tableName, String userName,
                                        String classPath, String uid, String tableStatus, float tableSize,
                                        String current_uid,String uid_list) throws IOException, ParseException {
        // 封住表描述信息
        TableDescribeEntity tableDescribeEntity = new TableDescribeEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCatLevel(4);
        categoryEntity.setLabel(tableName);
        categoryEntity.setParentId(pid);
        categoryEntity.setIsLeafs(1);
        categoryEntity.setIsDelete(0);
        categoryEntity.setUid(uid);
        categoryEntity.setStatus(tableStatus);
        categoryEntity.setUsername(userName);
        categoryEntity.setIsUpload("1");
        categoryEntity.setIsFilter("0");
        categoryEntity.setUidList(uid_list);
        System.out.println("==categoryEntity==" + categoryEntity );
        categoryMapper.insert(categoryEntity);
        UserLog userLog = new UserLog();
        userLog.setUsername(userName);
        userLog.setUid(Integer.parseInt(current_uid));
        userLog.setOpType("在category中增加了"+tableName);
        userLogService.save(userLog);
//        userLogService.insertLog(current_uid, 0, "在category中增加了"+tableName);


        tableDescribeEntity.setTableName(tableName);
        tableDescribeEntity.setTableId(categoryEntity.getId());
        tableDescribeEntity.setCreateUser(userName);
        // 解析系统当前时间
        tableDescribeEntity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        tableDescribeEntity.setClassPath(classPath);
        tableDescribeEntity.setUid(uid);
        tableDescribeEntity.setTableStatus(tableStatus);
        tableDescribeEntity.setTableSize(tableSize);
        tableDescribeEntity.setCheckApproving("");
        tableDescribeEntity.setCheckApproved("");
        tableDescribeMapper.insert(tableDescribeEntity);

        userLog.setOpType("在table_describe中上传了"+tableName);
        userLogService.save(userLog);
//        userLogService.insertLog(current_uid, 0, "在table_describe中上传了"+tableName);

        userMapper.minusTableSize(Integer.parseInt(uid), tableSize);
        userLog.setOpType("在user表中修改容量");
        userLogService.save(userLog);
//        userLogService.insertLog(current_uid, 0, "在user表中修改容量" );
        List<String> featureList = storeTableData(file, tableName);
        userLog.setOpType("在public模式下中创建了"+tableName);
        userLogService.save(userLog);
//        userLogService.insertLog(current_uid, 0, "在public模式下中创建了"+tableName);
        // 保存数据库
        System.out.println("表描述信息插入成功, 动态建表成功");
        return featureList;
    }

    @Override
    public List<TableDescribeEntity> selectAllDataInfo() {
        return tableDescribeMapper.selectAllDataInfo();
    }

    @Override
    public List<TableDescribeEntity> selectDataByName(String searchType, String name) {
        return tableDescribeMapper.selectDataByName(searchType, name);
    }

    @Override
    public TableDescribeEntity selectDataById(String id) {
        return tableDescribeMapper.selectDataById(id);
    }

    @Override
    public void deleteByTableName(String tableName) {
        tableDescribeMapper.deleteByTableName(tableName);
    }
    @Override
    public void deleteByTableId(String tableId) {
        tableDescribeMapper.deleteByTableId(tableId);
    }
    @Override
    public void updateById(String id, String[] pids, String tableName, String tableStatus) {
        TableDescribeEntity adminDataManage = tableDescribeMapper.selectById(id);  // 在table_describe表中获取表id对应的那一行数据

//        设置class_path
        String classPath = "公共数据集";
        for (String pid : pids){
            CategoryEntity categoryEntity = categoryMapper.selectById(pid);
            classPath += "/" + categoryEntity.getLabel();
        }
        classPath += "/" + tableName;

        adminDataManage.setClassPath(classPath);
        adminDataManage.setTableName(tableName);
        adminDataManage.setTableStatus(tableStatus);

        CategoryEntity categoryEntity = categoryMapper.selectById(adminDataManage.getTableId());
        categoryEntity.setParentId(pids[pids.length-1]);
        categoryMapper.updateById(categoryEntity); // 更改category表
        tableDescribeMapper.updateById(adminDataManage);// 更改table_describe表
    }

    @Override
    public void updateDataBaseTableName(String old_name, String new_name){
        tableDescribeMapper.updateDataBaseTableName(old_name, new_name);
    }

    @Override
    @Transactional
    public void updateInfo(String id, String tableid, String oldTableName, String tableName, String tableStatus, String[] pids,String current_uid) {
        updateById(id,pids, tableName, tableStatus);
        User user = userMapper.selectByUid(Integer.parseInt(current_uid));
        UserLog userLog = new UserLog();
        userLog.setUsername(user.getUsername());
        userLog.setUid(Integer.parseInt(current_uid));
        userLog.setOpType("更改了table_describe表中的"+oldTableName + "表为：" + tableName + ",将状态更改为：" + tableStatus + "并更改了classpath");
        userLogService.save(userLog);
//        userLogService.insertLog(current_uid, 0, "更改了table_describe表中的"+oldTableName + "表为：" + tableName + ",将状态更改为：" + tableStatus + "并更改了classpath");
        categoryMapper.updateTableNameByTableId(tableid, tableName, tableStatus);
        userLog.setOpType("更改了category表中的"+oldTableName + "表为：" + tableName + ",将状态更改为：" + tableStatus);
        userLogService.save(userLog);
//        userLogService.insertLog(current_uid, 0, "更改了category表中的"+oldTableName + "表为：" + tableName + ",将状态更改为：" + tableStatus);
        if (!oldTableName.equals(tableName)){
            updateDataBaseTableName(oldTableName, tableName);
            userLog.setOpType("更改了数据库中的"+oldTableName + "表为：" + tableName);
            userLogService.save(userLog);
//            userLogService.insertLog(current_uid, 0, "更改了数据库中的"+oldTableName + "表为：" + tableName );
        }
    }
    @Override
    public PageInfo<TableDescribeEntity> findByPageService(int pageNum, int pageSize, QueryWrapper<TableDescribeEntity> queryWrapper) {
        PageHelper.startPage(pageNum,pageSize);
        List<TableDescribeEntity> tableDescribeEntities = tableDescribeMapper.selectList(queryWrapper);
        return new PageInfo<>(tableDescribeEntities);
    }


}
