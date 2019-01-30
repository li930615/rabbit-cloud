package com.rabbit.common.bean.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

/**
 * @ClassName SwaggerConfig
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 18:35
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swaggerConfig.parm.authorization:true}")
    private boolean boolAuthorization;

    @Bean
    /**/
    public Docket createRestApi()
    {
        ParameterBuilder tokenBuilder = new ParameterBuilder();
        List parameterList = new ArrayList();
        if (this.boolAuthorization) {
            tokenBuilder.name("Authorization")
                    .defaultValue("去其他请求中获取heard中token参数")
                    .description("令牌")
                    .modelRef(new ModelRef("string"))
                    .parameterType("header")
                    .required(true)
                    .build();
            //将token放入api的入参中
            parameterList.add(tokenBuilder.build());
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为所有注解了@ApiOperation的类生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //所有请求
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameterList);
    }

    /*创建API的基本信息*/
    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
                .title("rabbit Swagger API ")
                .description("http://www.baidu.com")
                .termsOfServiceUrl("http://www.baidu.com")
                .contact(new Contact("rabbit", "http://www.baidu.com", "shijiusanqian@gmail.com"))
                .version("1.0")
                .build();
    }

    @Bean
    /*界面配置*/
    UiConfiguration uiConfig() {
        return new UiConfiguration(null, "list", "alpha", "schema", UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true,
                Long.valueOf(60000L));
    }
}
