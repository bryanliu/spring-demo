# 服务链路跟踪
本`Demo`演示了如何使用`Zipkin`来进行服务链路的跟踪
本`Demo` 有三个服务，`customer`, `waiter`, `barista`
`customer` 会下单给 `waiter`， `waiter` 会下单并且给b`arista`发送`neworder`的消息，`barista`收到消息后，做好咖啡后会发送消息给waiter告知咖啡已经做好

使用zipkin做服务跟踪调用
## POM
在三个服务中加上 ZIPKIN 的依赖
```xml
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-zipkin</artifactId>
   <version>2.2.8.RELEASE</version>
</dependency>
```
## 配置
在三个服务中加上 ZIPKIN的配置  
* 第一个配置比较明确，是地址   
* 第二个配置是采样率  
* 第三个配置是Sender 类型，这里我们先采用Web 的形式，稍后我们会采用Rabbit MQ的形式
```properties
# Trace
spring.zipkin.base-url=http://localhost:9411
spring.sleuth.sampler.probability=1.0
spring.zipkin.sender.type=web
```

## 运行
使用Docker启动 zipkin
```shell
docker run --name zipkin -d -p 9411:9411 openzipkin/zipkin
```

三个服务运行起来之后，调用customer的接口

```shell
ab -c 1 -n 1 -p /Users/admin/test/ab_post_data -T "application/json" http://localhost:8080/cust/order
```
就可以在 zipkin 的页面看到trace了
![img.png](source/img.png)

# 附录 1
使用Rabbit作为sender type
三个服务中都将SenderType变为
```properties
spring.zipkin.sender.type=rabbit
```
但是之前的zipkin是不会去监听队列的，所以我们要重启一个zipkin 让他和zipkin做个关联
```shell
docker run --name rabbit-zipkin -d -p 9411:9411 --link rabbitmq -e RABBIT_ADDRESSES=rabbitmq:5672 -e RABBIT_USER=spring -e RABBIT_PASSWORD=spring openzipkin/zipkin
```
这样Zipkin就能监听到队列的消息了。

对了，Customer 服务原来没有MQ的依赖的，也要加上相应的依赖和配置
