package com.todo.app.demo.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.todo.app.demo"))
//                .paths(PathSelectors.ant("/.**"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return appendTags(docket);
    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(DescriptionVariables.TODO_APP_MAIN,
                        "Used to operate with data - ToDo Task from the H2 Data Base"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ToDo App")
                .description("Demo project to show Intern skills in Java Development")
                .version("1.0")
                .build();
    }

}
