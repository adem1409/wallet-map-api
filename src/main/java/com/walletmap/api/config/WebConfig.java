package com.walletmap.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // @Autowired
    // private UserInterceptor userInterceptor;

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    // // Apply the interceptor to all routes
    // registry.addInterceptor(userInterceptor).addPathPatterns("/api/**");
    // }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map "/uploads/**" to the file system's "uploads/" directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/uploads/");
    }

}
