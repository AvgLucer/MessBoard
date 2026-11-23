
package com.messboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadLocation = Paths.get("uploads")
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*");
    }
}
