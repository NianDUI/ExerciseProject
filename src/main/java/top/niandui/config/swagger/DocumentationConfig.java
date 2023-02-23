//package top.niandui.config.swagger;
//
//import org.springframework.context.annotation.Primary;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Title: DocumentationConfig.java
// * @description: 服务API加入网关
// * @time: 2020/9/18 15:48
// * @author: liyongda
// * @version: 1.0
// */
//@Primary
////@Component
//public class DocumentationConfig implements SwaggerResourcesProvider {
//    @Override
//    public List<SwaggerResource> get() {
//        List<SwaggerResource> resources = new ArrayList<>();
//        resources.add(swaggerResource("小说", "/v2/api-docs", "1.0"));
////        resources.add(swaggerResource("统一指令平台", "/instruction-server/v2/api-docs", "2.0"));
//        return resources;
//    }
//
//    private SwaggerResource swaggerResource(String name, String location, String version) {
//        SwaggerResource swaggerResource = new SwaggerResource();
//        swaggerResource.setName(name);
//        swaggerResource.setLocation(location);
//        swaggerResource.setSwaggerVersion(version);
//        return swaggerResource;
//    }
//}
