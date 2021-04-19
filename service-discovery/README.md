#服务注册，发现示例
这个例子演示了使用Erueka 作为服务发现的服务器，将waiter-servier注册上去

Euraka Server
加上Euraka Server的依赖。
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
然后主类上加上 `EnableEurekaServer` 
配置
```properties
server.port=8761

#Eureka 服务自己不需要注册了
eureka.client.register-with-eureka=false
#也不去获取注册的服务
eureka.client.fetch-registry=false
```

就可以作为一个服务启动了。

## waiter-server
加上spring cloud 的 dependencyManagement 和 eureka-client dependency
```xml
<spring-cloud.version>2020.0.2</spring-cloud.version>
<!--要在 properties 中加上 spring cloud version-->

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

主类加上 `@EnableDiscoveryClient`

###配置
~~增加一个 bootstrap.properties, 加上配置（在spring cloud 2020.0。2中不用加这个文件了）~~这是一个碰到的坑  
在 application.properties 中加上
`spring.application.name=waiter-service`
这个就是服务在注册中心的名字

`application.properties` 中将`servier.port` 设为0，表示将随机选择一个端口
注册中心的地址暂时先不用配，使用默认`localhost:8761`就可以

### 运行
先启动 `eureka-server`， 再启动` waiter-servie`

打开 `http://localhost:8761/` 就可以看到`Eureka`的界面。上面看到有一个服务已经注册上去了。

然后运行 `waiter-service` 就可以看到 在`euraka` 界面看到了一个注册上去的服务。
> 试了一下多启动几个相同的服务，不过目前发现eureka 相同名字的服务只有一个，最新的会把原有的踢掉