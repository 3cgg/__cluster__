package me.libme.cls.rest.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@Profile({"dev","test","sit"})
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
    	
    	ParameterBuilder parameterBuilder = new ParameterBuilder();
    	parameterBuilder.name("_mock_enable")
        	.modelRef(new ModelRef("string"))
        	.parameterType("header")
        	.required(false)
        	.description("如果你需要mock，请设置值为true，否则留空")
        	.build();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameterBuilder.build());
    	
        return new Docket(DocumentationType.SWAGGER_2)
        		.globalOperationParameters(parameters)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .contact(new Contact("ONLY", "http://www.jianshu.com/p/8033ef83a8ed", "3cgg@163.com"))
                .description("所有参数涉及到多个值的，都是用逗号（，）隔开，除非特别指定")
                .version("1.0")
                .build();
    }
    
    
    
}
