nacos后台地址：http://47.115.205.56:8848/nacos

用户名：nacos 

密码：microservice



### 端口

个人信息微服务：810x

预约微服务：820x

审核微服务：830x

医院管理微服务：840x



### 技术选型

| 技术                 | 说明                  | 版本                     |
| :------------------- | :-------------------- | :----------------------- |
| Spring Cloud         | 微服务框架            | Spring Cloud Hoxton.SR12 |
| Spring Cloud Alibaba | 微服务框架            | 2.2.9.RELEASE            |
| Spring Boot          | 容器+MVC框架          | 2.3.12.RELEASE           |
| JDK                  |                       | 1.8                      |
| Nacos                | 服务注册发现+配置中心 | 2.1.0                    |
| Sentinel             | 服务熔断与限流        | 1.8.5                    |
| Seata                | 分布式事务            | 1.5.2                    |
| RocketMQ             | 消息队列              | 4.9.4                    |
| MyBatis-plus         | 数据库代码生成        | 3.2.0                    |
| MySQL                | 数据库                |                          |
|                      |                       |                          |
|                      |                       |                          |
|                      |                       |                          |
|                      |                       |                          |
|                      |                       |                          |
|                      |                       |                          |
|                      |                       |                          |
|                      |                       |                          |
|                      |                       |                          |





### 文件结构





### TODO

- [x] 整合nacos-discovery实现服务注册与发现
- [x] 整合mybatis-plus实现mapper和entity自动生成
- [x] 整合nacos-config实现配置管理
- [x] 整合swagger实现接口文档生成
- [x] 整合feign实现远程接口调用
- [ ] 整合loadbalance实现负载均衡
- [ ] 整合oauth2+jwt实现鉴权
- [ ] mybatis-generator共用
- [ ] 整合sentinel
- [ ] 整合gateway
- [ ] 验证码
- [ ] 日志
- [ ] 业务逻辑实现
- [ ] 自动化部署



