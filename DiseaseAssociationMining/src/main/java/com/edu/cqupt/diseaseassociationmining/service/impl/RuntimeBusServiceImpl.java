package com.edu.cqupt.diseaseassociationmining.service.impl;

import com.edu.cqupt.diseaseassociationmining.common.*;
import com.edu.cqupt.diseaseassociationmining.common.RuntimeTaskResponse;
import com.edu.cqupt.diseaseassociationmining.mapper.CommonEntityMapper;
import com.edu.cqupt.diseaseassociationmining.service.RuntimeBusService;
import com.edu.cqupt.diseaseassociationmining.service.RuntimeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class RuntimeBusServiceImpl implements RuntimeBusService {
    @Value("${algorithm.py.path1}")
    private String algorithmPyPath1;
    @Value("${algorithm.py.path2}")
    private String algorithmPyPath2;
    @Value("${algorithm.py.path3}")
    private String algorithmPyPath3;

    @Resource
    private RuntimeTaskService runtimeTaskService;

    @Autowired
    private CommonEntityMapper commonEntityMapper;
    @Override
    public RuntimeBusServiceResponse SF_DRMB(RuntimeBusCreateRequest request) throws Exception {
        RuntimeBusServiceResponse response=new RuntimeBusServiceResponse();
        String[] targets = request.getTargetcolumn();
        String[] CalculatedColumn = request.getFea();
        List<String> indexe_tar = new ArrayList<>();
        List<String>  indexe_fea = new ArrayList<>();
        for (String target : targets){

            Integer index = commonEntityMapper.findTargetColumnIndex(request.getTablename(),target);
            if (index != null){
                indexe_tar.add(String.valueOf(index));
            }
        }
        for (String fea : CalculatedColumn){
            Integer index = commonEntityMapper.findTargetColumnIndex(request.getTablename(),fea);
            if (index != null){
                indexe_fea.add(String.valueOf(index));
            }
        }
        List<String> args=new LinkedList<>();
        String targetcolumn = "--targetcolumn=" + String.join(" ", indexe_tar);
        String feacolumn = "--calculatedColumns=" + String.join(" ", indexe_fea);
        args.add(targetcolumn);
        args.add(feacolumn);
        args.add("--tableName="+request.getTablename());
        args.add("--K_OR="+request.getK_OR());
        args.add("--K_and_pc="+request.getK_and_pc());
        args.add("--K_and_sp="+request.getK_and_sp());


        RuntimeTaskRequest runtimeTaskRequest=new RuntimeTaskRequest();
        runtimeTaskRequest.setPyPath(algorithmPyPath3);
        runtimeTaskRequest.setArgs(args);
        RuntimeTaskResponse taskResponse=runtimeTaskService.submitTask(runtimeTaskRequest);
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse IAMB(RuntimeBusCreateRequest request) throws Exception {
        RuntimeBusServiceResponse response=new RuntimeBusServiceResponse();
        String[] targets = request.getTargetcolumn();
        String[] CalculatedColumn = request.getFea();
        List<String> indexe_tar = new ArrayList<>();

        List<String>  indexe_fea = new ArrayList<>();
        for (String target : targets){
            Integer index = commonEntityMapper.findTargetColumnIndex(request.getTablename(),target);
            if (index != null){
                indexe_tar.add(String.valueOf(index));
            }
        }
        for (String fea : CalculatedColumn){
            Integer index = commonEntityMapper.findTargetColumnIndex(request.getTablename(),fea);
            if (index != null){
                indexe_fea.add(String.valueOf(index));
            }
        }
        List<String> args=new LinkedList<>();
        String targetcolumn = "--targetcolumn=" + String.join(" ", indexe_tar);
        String feacolumn = "--calculatedColumns=" + String.join(" ", indexe_fea);
        args.add(targetcolumn);
        args.add(feacolumn);
        args.add("--tableName="+request.getTablename());


        RuntimeTaskRequest runtimeTaskRequest=new RuntimeTaskRequest();
        runtimeTaskRequest.setPyPath(algorithmPyPath1);
        runtimeTaskRequest.setArgs(args);
        RuntimeTaskResponse taskResponse=runtimeTaskService.submitTask(runtimeTaskRequest);
        response.setRes(taskResponse.getRes());
        return response;
    }
}
