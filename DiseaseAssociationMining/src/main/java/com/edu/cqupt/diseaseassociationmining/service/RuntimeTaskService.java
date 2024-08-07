package com.edu.cqupt.diseaseassociationmining.service;



import com.edu.cqupt.diseaseassociationmining.common.RuntimeTaskRequest;
import com.edu.cqupt.diseaseassociationmining.common.RuntimeTaskResponse;

public interface RuntimeTaskService {

    RuntimeTaskResponse submitTask(RuntimeTaskRequest request) throws Exception;

    RuntimeTaskResponse submitStastic(RuntimeTaskRequest request) throws Exception;



    
}
