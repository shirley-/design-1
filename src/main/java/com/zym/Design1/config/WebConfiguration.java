package com.zym.Design1.config;

import com.zym.Design1.interceptor.AdminInterceptor;
import com.zym.Design1.interceptor.LoginInterceptor;
import com.zym.Design1.interceptor.OpInterceptor;
import com.zym.Design1.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Autowired
    private OpInterceptor opInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).excludePathPatterns("/logout");
        // path 页面，需要登录才能访问
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/*");
        registry.addInterceptor(opInterceptor).addPathPatterns("/op/*");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/member/*");
        super.addInterceptors(registry);
    }


    //解决controller + RequestMapping ,静态资源路径问题
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/robots.txt").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/robots.txt");
        registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/templates/");
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
        super.addResourceHandlers(registry);
    }


}
