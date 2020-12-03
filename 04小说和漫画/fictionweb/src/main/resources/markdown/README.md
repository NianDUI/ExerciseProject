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

