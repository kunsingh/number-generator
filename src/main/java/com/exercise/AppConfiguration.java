package com.exercise;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class AppConfiguration {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.exercise"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointInfo());
    }

    private ApiInfo apiEndPointInfo() {
        return new ApiInfoBuilder().title("Number Generator")
                .description("A simple number generator in descending order for given start and steps to decrement")
                .version("1.0.0")
                .build();
    }
}
