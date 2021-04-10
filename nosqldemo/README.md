
# Redis 和 Jedis 的支持和测试

## 前置条件
用docker启动 Redis
```shell
docker run --name redis -d -p 6379:6379 redis
```

## 添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

## 配置
```properties
redis.host=localhost
redis.maxTotal=5
redis.maxIdle=5
redis.testOnBorrow=true
```
## 初始化
初始化两个Bean
```java
	@Bean
	@ConfigurationProperties("redis")
	public JedisPoolConfig jedisPoolConfig() {
		return new JedisPoolConfig();
	}

	@Bean(destroyMethod = "close")
	public JedisPool jedisPool(@Value("${redis.host}") String host) {
		return new JedisPool(jedisPoolConfig(), host);
	}
```

## 测试
然后就可以在测试里面 autowire jedisPool 进行测试了。
`@Autowired private JedisPool jedisPool;`
>注: Jedis 是非线程安全的，要从 jedispool 里面获取

```java
        Coffee c1 = Coffee.builder().name("test1").price(10).build();
        Coffee c2 = Coffee.builder().name("test2").price(20).build();
        // try with resource 的方式，不用显示关闭了
        try (Jedis jedis = jedisPool.getResource()) {
            Arrays.asList(c1, c2).forEach(c -> {
                jedis.hset("menu", c.getName(), Integer.toString(c.getPrice()));
                jedis.zadd("sortmenu", c.getPrice(), c.getName());
            });
            
            // 获取所有的key value pairs
            Map<String, String> res = jedis.hgetAll("menu");
            log.info(res.toString());
            assertEquals("20", res.get("test2"));
            
            // 根据特定的field 获取value
            String price = jedis.hget("menu", "test1");
            log.info(price);
            assertEquals("10", price);

            // 有序集合，可以根据score的范围查找，删除
            jedis.zrangeByScore("sortmenu", 10, 20).forEach(c -> log.info(c));
            jedis.zremrangeByScore("sortmenu", 10, 20);
            jedis.zrangeByScore("sortmenu", 10, 20).forEach(c -> log.info(c));

        }
```
