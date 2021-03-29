# FictionWebAPI
小说获取和下载Web页面API文档

# knife4j相关
## 1、新版本令牌授权方式
1. 添加全局Token
2. 文档管理 > 全局参数设置 > 添加相应数据
3. 在随便一个接口调用中使用AfterScript添加相应的数据。[AfterScript更详细的使用方法](https://gitee.com/xiaoym/knife4j/wikis/AfterScript)
```javascript
ke.global.setAllHeader("Token", "token值");
```

# SpringBoot相关
## 1、配置文件
### 1)、加载顺序(先->后)
#### 目录下加载顺序
- resources
- resources/config
- project | jarDir
- project/config | jarDir/config
#### 本项目中配置文件加载顺序
- resources/application.yml
  - application 使 server,knife4j,mybatis,redis,datasource,info 相关配置生效
- resources/config/application-(server,knife4j,mybatis,redis,datasource,info).yml
  - server 配置文件使 resources/config/application-(dev,run).yml 生效
- project/下配置文件(application.yml 和 application-**.yml)
- project/config/下配置文件(application.yml 和 application-**.yml)
  - application(-\**).yml 中的配置会覆盖之前加载的 application(-\**).yml 中相同配置
  - application-**.yml 中的配置会覆盖 application.yml 中相同配置
  - active 中的配置会覆盖 include 中相同配置

### 2)、文件示例
- application.yml
```yaml
spring:
  # 运行环境
  profiles:
    # 默认都包含的配置
    include: server,knife4j,mybatis,redis,datasource,info
    # 默认激活的配置
    active: dev
```
- config/application-dev.yml
```yaml
# dev环境配置
spring:
  redis:
    password: ''
  thymeleaf:
    # 禁用 thymeleaf 的缓存
    cache: false
```
### 3)、项目配置文件结构
```shell
│  pom.xml
├─config
│      application-datasource.yml
│      application-dev.yml
│
└─src
   └─main
      ├─java
      └─resources
          │  application.yml
          │  logback-spring.xml
          │
          ├─config
          │      application-datasource.yml
          │      application-dev.yml
          │      application-info.yml
          │      application-knife4j.yml
          │      application-mybatis.yml
          │      application-redis.yml
          │      application-run.yml
          │      application-server.yml
          │
          ├─mapper
          ├─markdown
          ├─sql
          ├─static
          └─templates
```
