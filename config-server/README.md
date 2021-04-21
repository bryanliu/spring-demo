# Config Server 相关Demo

要新建一个Spring boot 项目 作为 Config Server

## pom
加上 discovery 注册到服务发现，其他服务就可以通过服务名找到了
加上 spring-cloud-config-server
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
## 配置
* 这边选用consul相关配置，做相关配置
* 配置作为配置的git 的目录，可以选用本地目录做测试  
* 远端也是一样，建个repo就好
```properties
server.port=8888
spring.application.name=configserver

management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

# Consul 服务注册发现相关配置
spring.cloud.consul.port=8500
spring.cloud.consul.host=localhost
spring.cloud.consul.discovery.prefer-ip-address=true

spring.cloud.config.server.git.uri=file:///Users/admin/playgroupd/config-server-config-git
```


> 本地建立git项目方法如下
* 新建一个文件夹
* 在terminal 中进入这个目录，执行`git init`就可以了
* 将目录地址贴到配置项中
* 在目录中新建一个配置文件，比如`waiter-service.yml`
 * 这个要和目标服务名字对应
* 可以再建一个带profile的，比如`waiter-service-dev.yml`
 * config Server 会根据各种条件将配置项合并
## 运行
运行Config Server

### 查看配置项
相应的两个配置项我配置如下：
`waiter-service.yml`
```xml
coffee:
  discount: 80
  prefix: bry-
```
`waiter-service-dev.yml`
```xml
coffee:
  discount: 60
```

通过访问`http://localhost:8888` + 相应的服务名，profile就可以看到配置内容
比如访问 `http://localhost:8888/waiter-service-dev.properties` 可以看到
```properties
coffee.discount: 60
coffee.prefix: bry-
```
> 注意，虽然我没有在dev中配置coffee.prefix, 但是config-config仍然会从base中合并这个配置项

访问 `http://localhost:8888/waiter-service/dev` 可以看到
```json
{
"name": "waiter-service",
"profiles": [
"dev"
],
"label": null,
"version": "4ca35a956a1d16701354172303d35e33a163f3ca",
"state": null,
"propertySources": [
{
"name": "file:///Users/admin/playgroupd/config-server-config-git/waiter-service-dev.yml",
"source": {
"coffee.discount": 60
}
},
{
"name": "file:///Users/admin/playgroupd/config-server-config-git/waiter-service.yml",
"source": {
"coffee.discount": 80,
"coffee.prefix": "bry-"
}
}
]
}
```

# 应用服务配置
