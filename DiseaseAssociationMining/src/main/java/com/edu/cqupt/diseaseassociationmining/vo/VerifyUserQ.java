package com.edu.cqupt.diseaseassociationmining.vo;

import lombok.Data;

@Data
public class VerifyUserQ {
    private String username;
    private String q1; // 问题1   拼接答案
    private String q2;
    private String q3;
}
