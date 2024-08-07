package com.edu.cqupt.diseaseassociationmining.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.edu.cqupt.diseaseassociationmining.common.R;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.User;
import com.edu.cqupt.diseaseassociationmining.entity.UserLog;
import com.edu.cqupt.diseaseassociationmining.mapper.UserMapper;
import com.edu.cqupt.diseaseassociationmining.service.UserLogService;
import com.edu.cqupt.diseaseassociationmining.service.UserService;
import com.edu.cqupt.diseaseassociationmining.util.SecurityUtil;
import com.edu.cqupt.diseaseassociationmining.vo.InsertUserVo;
import com.edu.cqupt.diseaseassociationmining.vo.UpdateStatusVo;
import com.edu.cqupt.diseaseassociationmining.vo.UserPwd;
import com.edu.cqupt.diseaseassociationmining.vo.VerifyUserQ;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;

/**
 *
 * 用户管理模块
 *
 * 用户注册
 * 用户登录
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserLogService userLogService;


    @PostMapping("/getAll")
    public List<User> getAll(){
        return userService.list();
    }
    @GetMapping("/querUserNameExist")
    public R querUserNameExist(@RequestParam String userName){
        User existUser = userService.getUserByName(userName);
        if (existUser != null){
            return new R<>(500,"用户已经存在",null);
        }
        return new R(200, "用户名可用" , null);
    }

    @PostMapping("/signUp")
    public R signUp(@RequestBody User user) throws ParseException {

        System.out.println(user);
        // 检查用户名是否已经存在
        user.setUid(0);
        User existUser = userService.getUserByName(user.getUsername());
        if (existUser != null){
            return new R<>(500,"用户已经存在",null);
        }
        String pwd = user.getPassword();
        // 对密码进行加密处理
        String password = SecurityUtil.hashDataSHA256(pwd);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setUpdateTime(null);
        user.setRole(1);
        user.setUid(new Random().nextInt());
        user.setUploadSize(200D);
        userService.save(user);
        //  操作日志记录
        UserLog userLog = new UserLog();
        User one = userService.getUserByName(user.getUsername());
        Integer uid = one.getUid();
//       userLog.setId(new Random().nextInt());
        userLog.setUid(uid);
//        userLog.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userLog.setOpType("用户注册");
        userLogService.save(userLog);
        return new R<>(200,"成功",null);
    }

    @PostMapping("/login")
    public R login(@RequestBody User user, HttpServletResponse response, HttpServletRequest request){

        // 判断验证编码
        String code = request.getSession().getAttribute("code").toString();
        if(code==null) return R.fail(500,"验证码已过期！");
        if(user.getCode()==null || !user.getCode().equals(code)) {
            return R.fail(500, "验证码错误!");
        }

        String userName = user.getUsername();
        User getUser = userService.getUserByName(userName);

        if (getUser != null){
            String password = getUser.getPassword();
            // 用户状态校验
            // 判断用户是否激活
//            if (getUser.getUserStatus().equals("0")){
//                return R.fail("该账户未激活");
//            }
//            if (getUser.getUserStatus().equals("2")){
//                return R.fail("该账户已经被禁用");
//            }
            String userStatus = getUser.getUserStatus();
            if(userStatus.equals("0")){ // 待激活
                return R.fail(500,"账户未激活！");
            }else if(userStatus.equals("2")){
                return R.fail(500,"用户已被禁用!");
            }

            // 进行验证密码
            String pwd = user.getPassword();
            String sha256 = SecurityUtil.hashDataSHA256(pwd);
            if (sha256.equals(password)){
                // 验证成功
                UserLog userLog = new UserLog();
                userLog.setUid(getUser.getUid());
//                userLog.setOpTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                userLog.setOpType("登录系统");
                userLog.setUsername(userName);
                System.out.println("userlog:"+userLog);
                userLogService.save(userLog);
                // session认证
                HttpSession session = request.getSession();
                session.setAttribute("username",user.getUsername());
                session.setAttribute("userId",getUser.getUid());
                return new R<>(200,"登录成功",getUser);
            }else {
                return new R<>(500,"密码错误请重新输入",null);
            }
        }else {
            return new R<>(500,"用户不存在",null);
        }
    }


    @PostMapping("/logout")
    public R logout(HttpServletRequest request){

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        session.invalidate();
        return new R<>(200,"退出成功",null);
    }


    /**
     * 管理员中心查看得所有用户信息
     *
     * @return
     */
    @GetMapping("/allUser")
    public Map<String, Object> allUser(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize){
        return userService.getUserPage(pageNum, pageSize);

    }



    @GetMapping("/querUser")
    public List<User> querUser(){

        return userService.querUser();

    }

    /**
     *
     *  管理员修改用户状态
     * @return
     */
    @PostMapping("updateStatus")
    public R  updateStatus(@RequestBody UpdateStatusVo updateStatusVo){
        // 根据 id  修改用户状态   角色
        boolean b = userService.updateStatusById(updateStatusVo.getUid() ,updateStatusVo.getRole(),updateStatusVo.getUploadSize(), updateStatusVo.getStatus());
        if (b) return  R.success(200 , "修改用户状态成功");
        return  R.fail("修改失败");
    }


    @PostMapping("delUser")
    public R delUser(@RequestBody UpdateStatusVo updateStatusVo){

        Integer uid = updateStatusVo.getUid();
        boolean b = userService.removeUserById(uid);
        if (b) return R.success(200 , "删除成功");
        return R.fail(200 , "删除失败");
    }



    // TODO 目前不需要
    @PostMapping("insertUser")
    public R insertUser(@RequestBody InsertUserVo user) throws ParseException {
        boolean b = userService.insertUser(user);
        if (b) return R.success(200 , "删除成功");
        return R.fail(200 , "删除失败");
    }


    // 忘记密码功能
    @GetMapping("/queryQuestions")
    public R  forgotPwd(@RequestParam String username){
        User user = userService.getUserByName(username);
        String answer1 = user.getAnswer1().split(":")[0];
        String answer2 = user.getAnswer2().split(":")[0];
        String answer3 = user.getAnswer3().split(":")[0];
        List<String> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        return new R<>(200, "查询用户密保问题成功",answers );
    }


    // 验证问题


    @PostMapping("/verify")
    public R verify(@RequestBody VerifyUserQ verifyUserQ){
        // 用户名   密保问题 和 答案
        QueryWrapper queryWrapper = new QueryWrapper<>()
                .eq("username",verifyUserQ.getUsername())
                .eq("answer_1" , verifyUserQ.getQ1()).eq("answer_2" , verifyUserQ.getQ2()).eq("answer_3" , verifyUserQ.getQ3());
        User user = userService.getOne(queryWrapper);

        if (user == null){
            return R.fail("验证失败");
        }else {
            return R.success(200 ," 验证成功，请重置密码");
        }

    }

    @PostMapping("updatePwd")
    public R  updatePwd(@RequestBody UserPwd user){
        String password = user.getPassword();
        String sha256 = SecurityUtil.hashDataSHA256(password);
        user.setPassword(sha256);
        System.out.println(user);
        userService.updatePwd(user);
        return R.success(200 , "修改密码成功");
    }


    /**
     * 获取用户所有信息
     */
    @GetMapping("/getmessage/{uid}")
    public Result<List<User>> getMessage(@PathVariable("uid") Integer uid){
        User user = userMapper.selectById(uid);
        System.out.println(user);
        user.setPassword(null);
        return Result.success("200",user);
    }
    /**
     *  检查用户名是否重复
     * @param username
     * @return
     */
    @GetMapping("/checkRepetition/{username}")
    public Result checkRepetition(@PathVariable("username") String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        if(user != null){
            return Result.success(200, "用户名已存在");
        }else {
            return Result.success(200, "用户名可用");
        }
    }
    //修改个人信息
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        try {
            System.out.println("修改：");
            System.out.println(user);
            // 假设 userMapper 是 MyBatis 的一个 Mapper 接口
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("uid", user.getUid());
            int updatedRows = userMapper.update(user, wrapper);
            //  操作日志记录
            UserLog userLog = new UserLog();
            userLog.setUsername(user.getUsername());
            userLog.setUid(user.getUid());
            Date date = new Date();
            userLog.setCreateTime(date);

            if (updatedRows > 0) {
                // 更新成功，返回成功结果
                userLog.setOpType("用户修改个人信息成功");
                //userLog.setId(3333);
                System.out.println(userLog);
                userLogService.saveOrUpdate(userLog);
                return Result.success("200", "更新成功");
            } else {
                userLog.setOpType("用户修改个人信息失败");
                userLogService.save(userLog);
                // 更新失败，没有记录被更新
                return Result.success("404", "更新失败，用户不存在");
            }
        } catch (Exception e) {
            // 处理可能出现的任何异常，例如数据库连接失败等
            // 记录异常信息，根据实际情况决定是否需要发送错误日志
            // 这里返回一个通用的错误信息
            return Result.fail("500", "更新失败，发生未知错误");
        }
    }
    //修改密码，根据用户名匹配密码是否正确
    @PostMapping("/VerifyPas")
    public Result VerifyPas(@RequestBody Map<String, String> request){
        String username = request.get("username");
        String password = request.get("password");
        System.out.println(username);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User userDetails = new User();
        userDetails = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));

        if(!password.equals(userDetails.getPassword())){
            return Result.success(200,"密码不匹配",false);
        }
        return Result.success(200,"密码匹配",true);
    }

    //修改密码
    @PostMapping("/updatePas")
    public Result updatePas(@RequestBody Map<String, String> requests) {
        try {
            String username = requests.get("username");
            String password = requests.get("password");
            // 假设 userMapper 是 MyBatis 的一个 Mapper 接口
            int updatedRows = userMapper.updateByname(password, username);

            //  操作日志记录

            UserLog userLog = new UserLog();

            QueryWrapper queryWrapper1  = new QueryWrapper<>();
            queryWrapper1.eq("username",username);

            User one = userService.getOne(queryWrapper1);
            Integer uid = one.getUid();
            userLog.setUsername(username);
            userLog.setUid(uid);


            if (updatedRows > 0) {
                userLog.setOpType("用户修改密码成功");

                userLogService.save(userLog);
                // 更新成功，返回成功结果
                return Result.success("200", "更新成功");
            } else {
                userLog.setOpType("用户修改密码失败");
                userLogService.save(userLog);
                // 更新失败，没有记录被更新
                return Result.success("404", "更新失败，用户不存在或密码未更改");
            }
        } catch (Exception e) {
            String username = requests.get("username");
            UserLog userLog = new UserLog();
            QueryWrapper queryWrapper1  = new QueryWrapper<>();
            queryWrapper1.eq("username",username);
            User one = userService.getOne(queryWrapper1);
            Integer uid = one.getUid();
            // userLog.setId(1);
            userLog.setUid(uid);
            userLog.setOpType("用户修改密码失败，发生未知错误");
            userLogService.save(userLog);
            // 处理可能出现的任何异常，例如数据库连接失败等
            // 记录异常信息，根据实际情况决定是否需要发送错误日志
            // 这里返回一个通用的错误信息
            return Result.success("500", "更新失败，发生未知错误");
        }
    }

    //数据管理
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String searchUser
    ){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like(StringUtils.isNotBlank(searchUser),"username",searchUser);
        PageInfo<User> pageInfo = userService.findByPageService(pageNum, pageSize,queryWrapper);
        return Result.success(pageInfo);
    }

    // 新增可共享用户列表
    @GetMapping("/getTransferUserList")
    public Result getTransferUserList(@RequestParam("uid") String uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("uid", Integer.parseInt(uid));
        List<User> userList = userMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (User user : userList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", user.getUid());
            resultMap.put("label", user.getUsername());
            resultList.add(resultMap);
        }
        return  Result.success(200,"获得成功",resultList);
    }
}
