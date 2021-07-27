# FictionWebAPI
小说获取和下载Web页面API文档

[toc]

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
  - application 使 active(dev/run),inclue(server,knife4j,mybatis,redis,datasource,info) 相关配置生效
- resources/config/application-(server,knife4j,mybatis,redis,datasource,info).yml 配置生效
- project(或jar)/下配置文件(application.yml 和 application-**.yml)
- project(或jar)/config/下配置文件(application.yml 和 application-**.yml)
  - application(-\**).yml 中的配置会覆盖之前加载的 application(-\**).yml 中相同配置
  - application-**.yml 中的配置会覆盖 application.yml 中相同配置
  - include 中的配置会覆盖 active 中相同配置
  - springboot2.4会先加载 active => include

### 2)、文件示例
- application.yml
```yaml
spring:
  # 运行环境
  profiles:
    # 默认都包含的配置
    include: server,knife4j,mybatis,redis,datasource,info
    # 默认激活的配置。springboot2.4会先加载 active => include
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

# Feign配置
- [关于FeignClient的使用大全——进阶篇](https://www.cnblogs.com/sharpest/p/13709790.html)
- [Feign配置feign.client.default-to-properties原理详解](https://blog.csdn.net/yaomingyang/article/details/115558129)
## 超时时间
###  feign.hystrix.enabled: true
超时时间由 ```feign.client.config...``` 和 ```hystrix.command...``` 共同取最小生效。
- feign时间小： ```feign.client.config...``` 配置的生效
- hystrix时间小： ```hystrix.command...``` 的配置生效。
  - 默认的时间小：默认的生效，其他指定不生效
  - 默认的时间大：有指定指定的生效，否则默认的生效
```yaml
hystrix.command:
  #断路器的超时时间,下级服务返回超出熔断器时间，即便成功，消费端消息也是TIMEOUT,所以一般断路器的超时时间需要大于ribbon的超时时间。
  #服务的返回时间大于ribbon的超时时间，会触发重试
  default.execution.timeout.enabled: true
  # 默认熔断时（和指定的时间有关系：那个小那个生效）
  default.execution.isolation.thread.timeoutInMilliseconds: 40000
  # 访问密码模块熔断时长（和默认的时间有关系：那个小那个生效）
  passwd-server.isolation.thread.timeoutInMilliseconds: 20000
```
### feign.hystrix.enabled: false
超时时间由 ```feign.client.config...``` 单独生效。
```yaml
feign:
  # 启用熔断器
  hystrix.enabled: false
  # 客户端设置
  client.config:
    # 默认设置
    default:
      # 连接超时时间
      connectTimeout: 5000
      # 响应超时时间（和其他的响应时间没有关系，没有指定默认生效）
      readTimeout: 15000
    # 访问passwd-server服务：响应超时时间（和默认时间没有关系，指定单独生效）
    passwd-server.readTimeout: 20000
```

# Tomcat线程数
```yaml
# 最大连接数，默认为8192
server.tomcat.max-connections=10000
# 等待队列长度，默认100。队列也做缓冲池用，但也不能无限长，不但消耗内存，而且出队入队也消耗CPU
server.tomcat.accept-count=1000
# 最大工作线程数，默认200。（4核8g内存，线程数800，一般是核数*200。操作系统做线程之间的切换调度是有系统开销的，所以不是越多越好。）
server.tomcat.threads.max=800
# 最小工作空闲线程数，默认10。（适当增大一些，以便应对突然增长的访问量）
server.tomcat.threads.min-spare=100

# 查看tomcat线程数命令如下：
# 获取tomcat进程pid ：ps -ef | grep tomcat
# 统计该tomcat进程内的线程个数 ：ps -Lf 29295 | wc -l
```
