package com.edu.cqupt.diseaseassociationmining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.cqupt.diseaseassociationmining.entity.User;
import com.edu.cqupt.diseaseassociationmining.vo.InsertUserVo;
import com.edu.cqupt.diseaseassociationmining.vo.UserPwd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User getUerByUserName(@Param("userName") String userName);

    User getUserById(Integer id);

    void saveUser(@Param("user") User user);

    IPage<User> getAllUserInfo(Page<?> page);

    boolean updateStatusById(Integer uid,Integer role , double uploadSize, String status);

    void removeUserById(Integer uid);

    void insertUser(InsertUserVo user);

    List<User> selectUserPage(int offset, int pageSize);

    int countUsers();

    void updatePwd(UserPwd user);
//    修改密码
    Integer updateByname(String newpassword,String username);

    //    下面方法是管理员端-数据管理新增
    User selectByUid(Integer uid);
    void addTableSize(Integer uid, float tableSize);
    void minusTableSize(Integer uid, float tableSize);

//    数据管理新加
    @Update("UPDATE public.user SET upload_size = upload_size-#{size} WHERE uid = #{id}")
    int decUpdateUserColumnById(@Param("id") Integer id, @Param("size") Double size);

    @Update("UPDATE public.user SET upload_size = upload_size+#{size} WHERE uid = #{id}")
    int recoveryUpdateUserColumnById(@Param("id") Integer id, @Param("size") Double size);
}
