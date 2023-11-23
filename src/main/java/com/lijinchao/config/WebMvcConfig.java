package com.lijinchao.config;

import com.lijinchao.permission.AuthenticationInterceptor;
import com.lijinchao.service.UserService;
import com.lijinchao.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Value("${myConfig.staticResource.staticPath}")
//    private String staticPath;

    @Resource
    UserService userService;

    @Resource
    RedisUtil redisUtil;

    /**
     * 静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("file:"+staticPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor());
//        registry.addInterceptor(authenticationInterceptor())
////                表示拦截所有请求
////                .addPathPatterns("/**")
////                表示取消对特定路径的拦截
////                .excludePathPatterns("/home")
////                .addPathPatterns("/user/**")
////                .addPathPatterns("/submit/**")
//                .addPathPatterns("/user/**")
//                .addPathPatterns("/adminVerifier/**")
//                .excludePathPatterns("/user/register")
//                .excludePathPatterns("/user/login")
//                .excludePathPatterns("/user/emailLogin")
//                .excludePathPatterns("/adminVerifier/login")
////                .excludePathPatterns("/user/**")
////                这里一定不要写成/**/*.js的形式，spring boot无法识别
////                取消对static目录下静态资源的拦截
//                .excludePathPatterns("/static/**");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor(userService, redisUtil);
    }


}
