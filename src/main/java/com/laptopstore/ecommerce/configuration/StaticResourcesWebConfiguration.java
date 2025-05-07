package com.laptopstore.ecommerce.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {
    @Value("${files.path}")
    private String filesPath;

    @Value("${static.resources.mapping.folder}")
    private String staticResourcesFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path basePath = Paths.get(filesPath).resolve(staticResourcesFolder);

        registry.addResourceHandler( staticResourcesFolder + "/**")
                .addResourceLocations("file:" + filesPath + "/" + staticResourcesFolder + "/");
    }
}
