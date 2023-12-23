### 提交修改流程

#### 提交前先git pull，没冲突再push！！！！！！！！

1. 在自己对应微服务的目录下，编写自己的代码。
2. 添加maven依赖时，在自己微服务的pom中添加，不用写version，统一从父项目中拿，没有时在父项目的dependenyManager中添加，防止版本不一致产生问题
3. 代码请遵循MVC架构，controller负责调用，service负责业务逻辑，mapper负责操作数据库
4. 返回结果统一使用CommentResult类封装，该类的定义在common包中。如果需要添加由其他多个微服务共用的功能，也请添加在common包中
5. mapper、entity使用mybatis-plus生成，参考 personal-info-service/src/main/java/com/microservice/personalinfoservice/MyBatisGenerator.java，将MyBatisGenerator.java复制到自己的包中，需要修改的地方我已注释给出。注意数据库修改后重新生成不会覆盖原文件，需要删除原文件后重新生成
6. controller添加swagger注解，参考个人信息微服务
7. 如果需要调用其他服务的方法，请使用feign，参考appointment-service/src/main/java/com/microservice/appointmentservice/service/HospitalManageService，（这个是我写的用法示例，不一定符合实际情况）



### 各中间件

nacos后台地址：http://47.115.205.56:8848/nacos

用户名：nacos 

密码：microservice



redis地址：http://47.115.205.56:6379

密码：microservice



rabbitmq

http://47.115.205.56:5672/

管理界面：http://47.115.205.56:15672/

用户名：rabbitmq

密码：microservice



sentinel

管理界面：http://47.115.205.56:8858/

用户名：sentinel

密码：sentinel



jenkins

管理界面：http://47.117.145.92:8081/

用户名：admin

密码：microservice



### 端口

个人信息微服务：810x

预约微服务：820x

审核微服务：830x

医院管理微服务：840x



认证微服务：910x

网关：9201



### 技术选型

| 技术                 | 说明                  | 版本                     |
| :------------------- | :-------------------- | :----------------------- |
| Spring Cloud         | 微服务框架            | Spring Cloud Hoxton.SR12 |
| Spring Cloud Alibaba | 微服务框架            | 2.2.9.RELEASE            |
| Spring Boot          | 容器+MVC框架          | 2.3.12.RELEASE           |
| JDK                  |                       | 1.8                      |
| Nacos                | 服务注册发现+配置中心 | 2.1.0                    |
| Sentinel             | 服务熔断与限流        | 1.8.0                    |
| Seata                | 分布式事务            | 1.5.2                    |
| RabbitMQ             | 消息队列              | 3.7.14                   |
| MyBatis-plus         | 数据库代码生成        | 3.2.0                    |
| MySQL                | 数据库                | 5.7                      |
| Redis                | 缓存数据库            | 7.0.12                   |
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
- [x] 整合oauth2+jwt实现鉴权
- [x] 整合gateway实现网关服务
- [ ] 整合sentinel
- [ ] 整合seata
- [ ] 不要明文密码
- [ ] mybatis-generator共用
- [ ] 验证码
- [ ] 日志
- [ ] 业务逻辑实现
- [ ] 自动化部署



