package com.edu.cqupt.diseaseassociationmining.service;

import com.edu.cqupt.diseaseassociationmining.common.RuntimeBusCreateRequest;
import com.edu.cqupt.diseaseassociationmining.common.RuntimeBusServiceResponse;

public interface RuntimeBusService {
    RuntimeBusServiceResponse SF_DRMB(RuntimeBusCreateRequest request) throws Exception;

    RuntimeBusServiceResponse IAMB(RuntimeBusCreateRequest request) throws Exception;
}
