package com.pbo.telor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.environment}")
    private String appEnvironment;

    @Value("${file.upload-dir}")
    private String productionUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String finalUploadDir;

        if ("development".equalsIgnoreCase(appEnvironment)) {
            finalUploadDir = System.getProperty("user.dir") + "/uploads/";
        } else if ("production".equalsIgnoreCase(appEnvironment)) {
            finalUploadDir = productionUploadDir;
        } else {
            throw new IllegalArgumentException("Invalid app environment: " + appEnvironment);
        }

        if (!finalUploadDir.endsWith("/") && !finalUploadDir.isEmpty()) {
            finalUploadDir += "/";
        }

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("file:" + finalUploadDir);
    }
}
