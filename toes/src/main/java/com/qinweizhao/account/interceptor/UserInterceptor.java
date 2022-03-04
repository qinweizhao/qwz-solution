package com.qinweizhao.account.interceptor;

import com.qinweizhao.account.constant.UserConstant;
import com.qinweizhao.account.entity.User;
import com.qinweizhao.account.exception.GlobalException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/31 11:13
 * </p>
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<String> user = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/swagger-ui/**", requestURI);
        if (match){
            return true;
        }
        User sessionUser = (User) request.getSession().getAttribute(UserConstant.LOGIN_USER);
        if (sessionUser==null){
            throw new GlobalException("尚未登录，请先登录");
        }
        user.set(sessionUser.getUsername());
        return true;
    }
}
