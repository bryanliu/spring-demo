package com.bry.coffeeshopjpa.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PerformanceIntecepter())
                .addPathPatterns("/coffee/**");//可以指定拦截的路径
    }
}
