# 消息驱动应用 
本练习将会演示 waiter Service 发送消息给 barista Service发送消息

## Waiter Service 
### Pom
加上Rabbit MQ 的依赖

### 配置
#### 启动Rabbit MQ
使用`Docker` 启动 `RabbitMQ with management`  
```shell
docker run --name rabbitmq -d -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=spring -e RABBITMQ_DEFAULT_PASS=spring rabbitmq:3.7-management
```

加上Rabbit MQ 的连接信息  
加上绑定的相关信息和消费组
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=spring
spring.rabbitmq.password=spring

# channel: finishedOrders, group: waiter-service
spring.cloud.stream.bindings.finishedOrders.group=waiter-service
```

### 程序修改
定义一个Barista，声明Stream，可以看到一个是我们要监听的Stream  
一个是我们会发送到的Stream
```java
public interface Barista {

    String NEW_ORDRS = "newOrders";
    String FINISHED_ORDRS = "finishedOrders";

    @Input
    SubscribableChannel finishedOrders();

    @Output
    MessageChannel newOrders();
}

```

在主类上加上 @EnableBinding(Barist.class)
```java
@EnableBinding(Barista.class)
public class CoffeeshopWaiterServiceInMQ {
```

然后就可以在程序中发送消息
```java
//Send message
barista.newOrders().send(MessageBuilder.withPayload(order.getId()).build());
```


监听消息
```java
@Component
@Slf4j
public class BaristaListener {

    @StreamListener(Barista.FINISHED_ORDRS)
    public void finishedOrder(Integer id) {
        log.info("Get finished orders {}", id);
    }
}
```

### 运行
这个Demo 我们做的是在下单的时候我们会发送消息到Barista服务，Barista服务监听之后，做相关逻辑，然后发送消息到`finishedOrder`队列。这个队列waiter 服务会监听

所以我们调用订单接口，就会看到相应的消息发送和接收

## 使用Kafka
### POM
将stream-rabbit 替换为 binder-kafka
```xml
<!--		<dependency>-->
<!--			<groupId>org.springframework.cloud</groupId>-->
<!--			<artifactId>spring-cloud-starter-stream-rabbit</artifactId>-->
<!--		</dependency>-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

### 配置
启动Kafka，Kafka没有单独的镜像，可以用一下DockerComposer 启动
```yaml
---
version: '2'
services:
  zookeeper:
    image: zookeeper:latest

  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - 9092:9092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```
properties
其实下面的都是默认的，不过还是显式声明一下
```properties
#kafka
spring.cloud.stream.kafka.binder.brokers=localhost
spring.cloud.stream.kafka.binder.defaultBrokerPort=9092
```

### 程序修改
程序没什么要修改的，不过这次发送消息改成声明的方式，注意方法要有返回值
```java
    @StreamListener(Waiter.NEW_ORDRS)
    @SendTo(Waiter.FINISHED_ORDRS)//使用这个Annotation
    public Integer newOrders(Integer id) throws InterruptedException {
        log.info("Get new orders {}", id);

        //Just echo back
        //Thread.sleep(10); //Sleep as process time

        return id;// 要有返回值

    }
```
