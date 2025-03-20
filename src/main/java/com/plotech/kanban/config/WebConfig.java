package com.plotech.kanban.config;

import com.plotech.kanban.interceptor.LoginCheckInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于配置Spring MVC的相关设置，如跨域资源共享（CORS）和拦截器。
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /**
     * 登录检查拦截器。
     */
    private final LoginCheckInterceptor loginCheckInterceptor;

    /**
     * 添加跨域资源共享（CORS）映射。
     *
     * @param registry CORS注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOriginPatterns("*")
                // 允许所有HTTP方法
                .allowedMethods("*")
                // 允许所有请求头
                .allowedHeaders("*")
                // 允许携带凭证
                .allowCredentials(true)
                // 设置预检请求的有效期（秒）
                .maxAge(3600);
    }

    /**
     * 添加拦截器。
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                // 添加拦截路径
                .addPathPatterns("/**")
                // 排除登录和注册路径，还有积木报表相关的路径
                .excludePathPatterns("/api/signIn", "/jmreport/**", "/drag/**", "/jimubi/**");
    }
}