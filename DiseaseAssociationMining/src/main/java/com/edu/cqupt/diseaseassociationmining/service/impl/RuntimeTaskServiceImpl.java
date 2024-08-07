package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.edu.cqupt.diseaseassociationmining.common.RuntimeTaskRequest;
import com.edu.cqupt.diseaseassociationmining.common.RuntimeTaskResponse;
import com.edu.cqupt.diseaseassociationmining.service.adapter.RuntimeServiceTaskAdapter;
import com.edu.cqupt.diseaseassociationmining.tool.PythonRun;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class RuntimeTaskServiceImpl extends RuntimeServiceTaskAdapter {

    @Resource
    private PythonRun pythonRun;

    @Override
    public RuntimeTaskResponse submitTask(RuntimeTaskRequest request) throws Exception {
        RuntimeTaskResponse taskResponse = new RuntimeTaskResponse();
        BeanUtils.copyProperties(request, taskResponse);
        taskResponse.setTaskStartTime(new Date().getTime());
        System.out.println("接收到的PyPath"+request.getPyPath());
        System.out.println("接收到的Args"+request.getArgs());
        //任务信息入库
        //提交任务
        taskResponse.setTaskFinishTime(new Date().getTime());
        String a1 = pythonRun.run(request.getPyPath(), request.getArgs());
        System.out.println("a1="+a1);
        List<String> res =JSONObject.parseArray(pythonRun.run(request.getPyPath(), request.getArgs()),String.class);
        taskResponse.setRes(res);
        System.out.println("res"+res);
        return taskResponse;
    }


    @Override
    public RuntimeTaskResponse submitStastic(RuntimeTaskRequest request) throws Exception {
        RuntimeTaskResponse taskResponse = new RuntimeTaskResponse();
        BeanUtils.copyProperties(request, taskResponse);
        String resultStr = pythonRun.run(request.getPyPath(), request.getArgs());
        List<String> res = new ArrayList<>();
        res.add(resultStr);
        taskResponse.setRes(res);
        return taskResponse;
    }
}
