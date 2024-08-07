package com.edu.cqupt.diseaseassociationmining.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserVo {


    private String username;

    private String password;

    private Date createTime;

    private Date updateTime;

    private Integer role;

    private String userStatus;

}