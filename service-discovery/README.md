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
> 

## Custoemr Service 
创建一个Customer的项目
也要依赖
Discovery-Client
并且作为一个Web项目

### restTempate
由于之前的课说过，RestTempate没有自动配置，所以需要手动创建一个。
并且由于要用到Load balance所以标注为 `@Loadbalanced` (如果没有这个标注，用服务名访问会显示找不到)
```java
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .build();
    }
```
当然，这边可以将RestTemplate底层换为HTTPClient，并且设置连接池，这个放到附录中。

### 然后就可以调用远程服务了
注意看，通过discoveryClient 可以获得远程服务的Server list  
用restTemplate + 服务名就可以请求了
```java
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    @Override public void run(ApplicationArguments args) throws Exception {

        discoveryClient.getInstances("waiter-service")
                .forEach(s -> log.info("waiter service: hots {} port {}", s.getHost(), s.getPort()));
        getMenu();
    }

    void getMenu() {
        ParameterizedTypeReference<List<Coffee>> cls = new ParameterizedTypeReference<List<Coffee>>() {
        };

        ResponseEntity<List<Coffee>>
                list = restTemplate.exchange("http://waiter-service/coffee/all", HttpMethod.GET, null, cls);
        list.getBody().forEach(coffee -> log.info("{}", coffee));

    }
```

## 附录
定制化httpTemplate
### 首先加上HTTPClient的依赖
```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.7</version>
</dependency>
```
然后创建`requestFactory` 并且将r`equestFactor`设置到 `resttempalte`中。
```java
    @Bean
    public HttpComponentsClientHttpRequestFactory requestFactory() {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return requestFactory;
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(100))
                .setReadTimeout(Duration.ofMillis(500))
                .requestFactory(this::requestFactory)
                .build();
    }
```
这是一个比较简单的版本，还可以针对各种策略进行定制，比如keep-alive, 最大连接数等。这个等后面研究吧。

