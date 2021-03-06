package top.niandui.common.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import top.niandui.common.uitls.GetInfo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 16:02
 */
@Configuration
//@EnableKnife4j
@EnableSwagger2WebMvc
//@EnableSwagger2WebFlux
@Import(BeanValidatorPluginsConfiguration.class) // 提供Knife4j的请求参数对JSR303支持
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(OpenApiExtensionResolver openApiExtensionResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(GetInfo.SERVICE_NAME)
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.niandui.controller"))
                .paths(PathSelectors.any())
                .build()
                // 赋予插件体系
                .extensions(openApiExtensionResolver.buildExtensions(GetInfo.SERVICE_NAME))
                .extensions(openApiExtensionResolver.buildSettingExtensions())
                // 添加全局Token
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                // 时间转字符串
                .directModelSubstitute(Timestamp.class, String.class)
                .directModelSubstitute(Date.class, String.class);
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("令牌", "Token", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex(".*"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Token", authorizationScopes));
        return securityReferences;
    }

    /**
     * Api信息
     *
     * @return Api信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("FictionWebAPI")
                .contact(new Contact("niandui", "http://www.niandui.top/", ""))
                .version("1.0")
                .description("小说获取和下载")
                .build();
    }
}
