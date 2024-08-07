package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.cqupt.diseaseassociationmining.entity.AlgorithmUsageDailyStats;
import com.edu.cqupt.diseaseassociationmining.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    List<AlgorithmUsageDailyStats> getAlgorithmUsageDailyStatsLast7Days();

    List<String> getAlgorithmName();


    List<Task> getTaskList();
    Task getlistbyId(Integer id);

    void deleteTask(int id);

    void addTask(Task task);

//
//    List<Task> getTaskListByConditions(String leader, String dataset, String disease);
}