package com.qinweizhao.account.service;

import com.qinweizhao.account.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.account.utils.Result;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Monday_1201
 * @since 2021-03-31
 */
public interface UserService extends IService<User> {

    Result login(User user, HttpSession session);

    Result logout(HttpSession session);
}
