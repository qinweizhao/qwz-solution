package com.qinweizhao.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qinweizhao.account.entity.User;
import com.qinweizhao.account.interceptor.UserInterceptor;
import com.qinweizhao.account.mapper.UserMapper;
import com.qinweizhao.account.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.account.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Monday_1201
 * @since 2021-03-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    UserMapper userMapper;


    @Override
    public Result login(User user, HttpSession session) {
        String username = user.getUsername();
        User userDb = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (userDb==null){
            return Result.error("用户名错误");
        }
        if (!user.getPassword().equals(userDb.getPassword())){
            return Result.error("密码错误");
        }
        session.setAttribute("username",userDb);
        return Result.ok("登录成功");
    }

    @Override
    public Result logout(HttpSession session) {
        String username = UserInterceptor.user.get();
        if (username==null){
            return Result.error("尚未登录");
        }
        session.removeAttribute(username);
        return Result.ok("注销成功");
    }
}
