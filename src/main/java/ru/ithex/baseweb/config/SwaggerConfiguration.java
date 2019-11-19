package ru.ithex.baseweb.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket swagger(@Value("${app.name}") String name){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(name)
                        .build())
                .select()
                .apis(RequestHandlerSelectors.any())
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                .apis(RequestHandlerSelectors.basePackage("ru.ithex.controllers"))
//                .apis(input -> !input. supportedMethods().contains(RequestMethod.OPTIONS))
                .paths(Predicates.not(PathSelectors.regex("/error*")))
                .build();
    }
}