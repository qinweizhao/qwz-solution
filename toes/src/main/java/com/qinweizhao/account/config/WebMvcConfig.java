package com.qinweizhao.account.config;

import com.qinweizhao.account.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/31 11:28
 * </p>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    UserInterceptor userInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(userInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/user/logout")
//                .excludePathPatterns("/user/login");
//    }
}
