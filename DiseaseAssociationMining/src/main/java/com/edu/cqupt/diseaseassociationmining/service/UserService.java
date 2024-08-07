package com.edu.cqupt.diseaseassociationmining.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.cqupt.diseaseassociationmining.entity.User;
import com.edu.cqupt.diseaseassociationmining.vo.InsertUserVo;
import com.edu.cqupt.diseaseassociationmining.vo.UserPwd;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    List<User> getAll();

    User getUserByName(String userName);

    User getUserById(Integer id);

    void saveUser(User user);

    Page<User> getAllUserInfo(int pageNum  , int pageSize);

    boolean updateStatusById(Integer uid, Integer role ,double uploadSize, String status);

    boolean removeUserById(Integer uid);

    boolean insertUser(InsertUserVo user);

    Map<String, Object> getUserPage(int pageNum, int pageSize);

    boolean updatePwd(UserPwd user);

    List<User> querUser();
    //    下面方法是管理员端-数据管理新增
    void addTableSize(String uid, float tableSize);
    void minusTableSize(String uid, float tableSize);

    //数据管理
    PageInfo<User> findByPageService(Integer pageNum, Integer pageSize, QueryWrapper<User> queryWrapper);

    User getUserByUserName(String username);
}
