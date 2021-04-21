# 断路器
在 spring cloud 3 中 Hystrix 已经不再被支持。所以做了一个`resilience4j`的支持

## pom
```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot2</artifactId>
    <version>1.7.0</version>
</dependency>
```

## 配置
可以在配置文件中针对每个circuitbreaker 做配置，下面的menu 就是我在调用的地方定义的断路器名字
```properties
resilience4j.circuitbreaker.backends.menu.failure-rate-threshold=50
resilience4j.circuitbreaker.backends.menu.wait-duration-in-open-state=5000
resilience4j.circuitbreaker.backends.menu.ring-buffer-size-in-closed-state=5
resilience4j.circuitbreaker.backends.menu.ring-buffer-size-in-half-open-state=3
resilience4j.circuitbreaker.backends.menu.event-consumer-buffer-size=10
```

## 调用
两种方式调用，一种是annotation, 一种是程序的方式，程序的方式可以设置fallback。
> circuitBreaker 是通过构造方法注入进去的
```java
    private io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;

    public CoffeeController(CircuitBreakerRegistry registry) {
        circuitBreaker = registry.circuitBreaker("menu");
    }

    @GetMapping("/")
    //@HystrixCommand(fallbackMethod = "fallbackGetCoffee")

    public List<Coffee> getAllCoffee() {
        return Try.ofSupplier(
                io.github.resilience4j.circuitbreaker.CircuitBreaker.decorateSupplier(
                        circuitBreaker, () -> coffeeService.getAll()))
                //.recover(Exception.class, Collections.emptyList()) //两种写法都可以
                .recover(throwable -> {
                    log.error("error happens when call get all coffees", throwable);
                    return Collections.emptyList();
                })
                .get();
    }

    @PostMapping("/order")
    @CircuitBreaker(name = "order")
    public CoffeeOrder addOrder(@RequestBody NewOrderRequest order) {
        return coffeeOrderService.addOrder(order);
    }
```

## 监控
项目添加Accuator，就可以看到相关Metrix
打开acuator 通过地址 `http://localhost:8080/actuator/circuitbreakers` ， 可以看到当前circutbreakers 的节点名字，

通过`http://localhost:8080/actuator/circuitbreakerevents/` 可以看到相关事件
可以看到前两个事件是调用成功，后面是调用失败的事件。
```json
[{
  "circuitBreakerName": "order",
  "type": "SUCCESS",
  "creationTime": "2021-04-20T22:18:07.673+08:00[Asia/Shanghai]",
  "errorMessage": null,
  "durationInMs": 353,
  "stateTransition": null
},
{
"circuitBreakerName": "menu",
"type": "SUCCESS",
"creationTime": "2021-04-20T22:18:10.483+08:00[Asia/Shanghai]",
"errorMessage": null,
"durationInMs": 20,
"stateTransition": null
},
{
"circuitBreakerName": "order",
"type": "ERROR",
"creationTime": "2021-04-20T22:19:45.931+08:00[Asia/Shanghai]",
"errorMessage": "feign.FeignException$ServiceUnavailable: [503] during [POST] to [http://waiter-service/order/order] [CoffeeOrderService#addOrder(NewOrderRequest)]: [Load balancer does not contain an instance for the service waiter-service]",
"durationInMs": 4,
"stateTransition": null
},
{
"circuitBreakerName": "order",
"type": "ERROR",
"creationTime": "2021-04-20T22:19:47.020+08:00[Asia/Shanghai]",
"errorMessage": "feign.FeignException$ServiceUnavailable: [503] during [POST] to [http://waiter-service/order/order] [CoffeeOrderService#addOrder(NewOrderRequest)]: [Load balancer does not contain an instance for the service waiter-service]",
"durationInMs": 8,
"stateTransition": null
}]
```

# 限流
还是使用 `resilience4j` 的Bulkhead 对接口进行限流
## pom
pom 文件不用变

## 代码修改
加上一个BulkheadRegistry，然后生成一个`Bulkhead`
在需要限流的地方加上 `@Bulkhead`的标注
```java
public CoffeeController(CircuitBreakerRegistry registry,
        BulkheadRegistry bulkheadRegistry) {
    circuitBreaker = registry.circuitBreaker("menu");
    bulkhead = bulkheadRegistry.bulkhead("menu");
}

@io.github.resilience4j.bulkhead.annotation.Bulkhead(name = "order")
public CoffeeOrder addOrder(@RequestBody NewOrderRequest order) {
        return coffeeOrderService.addOrder(order);
        }

```
当然也可以编程的方式，注意看外面又套了一层`Buldhead.decorateSupplier`, 整成套娃了。
```java
    public List<Coffee> getAllCoffee() {
        return Try.ofSupplier(
                Bulkhead.decorateSupplier(bulkhead,
                        CircuitBreaker.decorateSupplier(circuitBreaker, () -> coffeeService.getAll())))
                .recover(Exception.class, Collections.emptyList()) //两种写法都可以
                //                .recover(throwable -> {
                //                    log.error("error happens when call get all coffees", throwable);
                //                    return Collections.emptyList();
                //                })
                .recover(BulkheadFullException.class, Collections.emptyList())
                .get();
    }
```


## 配置
```properties
# 允许两个并发的Call
resilience4j.bulkhead.backends.order.max-concurrent-calls=2
# 最多等5ms
resilience4j.bulkhead.backends.order.max-wait-duration=5ms
```


## 运行
使用ab 命令 进行并发测试
```shell
ab -c 50 -n 200 -p ab_post_data -T "application/json" http://localhost:8080/coffee/order
```
> `ab` 是个`linux`自带的并发测试工具，`-c` 表示并发数 `-n` 表示请求总数
> 
> 对于`post` 要加另外两个参数，`-p` 表示post的数据， `-t` 表示请求数据类型

就可以看到后台已经在抛出ERROR
`io.github.resilience4j.bulkhead.BulkheadFullException: Bulkhead 'order' is full and does not permit further calls
`

查看 Acuator http://localhost:8080/actuator/bulkheadevents
也可以看到相应的Event
```json
[
    {
      "bulkheadName": "order",
      "type": "CALL_PERMITTED",
      "creationTime": "2021-04-21T10:46:40.169+08:00[Asia/Shanghai]"
    },
    {
      "bulkheadName": "order",
      "type": "CALL_FINISHED",
      "creationTime": "2021-04-21T10:46:40.187+08:00[Asia/Shanghai]"
    },
    {
      "bulkheadName": "order",
      "type": "CALL_PERMITTED",
      "creationTime": "2021-04-21T10:46:40.194+08:00[Asia/Shanghai]"
    },
    {
      "bulkheadName": "order",
      "type": "CALL_REJECTED",
      "creationTime": "2021-04-21T10:46:40.200+08:00[Asia/Shanghai]"
    }
]
```