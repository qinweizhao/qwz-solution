package com.qinweizhao.account.controller;


import com.qinweizhao.account.entity.User;
import com.qinweizhao.account.service.UserService;
import com.qinweizhao.account.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Monday_1201
 * @since 2021-03-31
 */
@Api(tags = "用户操作")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpSession session){
        Result result = userService.login(user,session);
        return  result;
    }

    @GetMapping("allUser")
    public Result allUser(){
        List<User> list = userService.list();
        return Result.ok("成功").setObj(list);
    }

    @ApiOperation("注销")
    @GetMapping("/logout")
    public Result logout(HttpSession session){
        Result result = userService.logout(session);
        return  result;
    }

}
