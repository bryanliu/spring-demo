# Mondb 支持和测试
##前置条件
通过docker启动mongodb
```shell
docker run --name mongo -p 27017:27017 -v /Users/admin/Docker/mongodb:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin
-e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo
```
登陆到mongodb 的shell， 初始化 数据库和用户名
```shell
mongo -u admin -p admin #登陆

#mongodb中创建库
use springbucks;

#创建User
db.createUser(
    {
        user: "springbucks",
        pwd: "springbucks",
        roles: [
            { role: "readWrite", db: "springbucks" }
        ]
    }
)
```
## 添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```
## 配置
在配置文件中配置 连接信息, 并打开日志，方便调试
```properties
spring.data.mongodb.uri=mongodb://springbucks:springbucks@localhost:27017/springbucks
#显示MongoDB的日志，注意路径是本项目mapper的路径
logging.level.org.springframework.data.mongodb.core=debug
```
在启动类添加标注 `@EnableMongoRepositories` （其实也可以不加，自动配置了）

创建Model，标注为`@Document` 以及 标注对应的ID `@Id`
```java
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coffee {
    @Id
    private String id;
    private String name;
    private Integer price;
    private Date createTime;
    private Date updateTime;
}
```

## 测试
### 使用 MongoRepository
也支持带语义的方法名，还自带了很多方法
```java
public interface CoffeeMongodbRepository extends MongoRepository<Coffee, String> {

    List<Coffee> findByName(String name);
}
```
```java
// 保存
espresso = coffeeMongodbRepository.save(espresso);

// 查找
List<Coffee> res = coffeeMongodbRepository.saveAll(Arrays.asList(espresso, latte));

// 根据ID查找
Optional<Coffee> res = coffeeMongodbRepository.findById(espresso.getId());

//Exampe 和 Matcher 查询
ExampleMatcher matcher = ExampleMatcher.matching()
.withIgnorePaths("price")
.withMatcher("name", exact())
//.withMatcher("price", exact())
;
Coffee criteria = Coffee.builder()
.price(200) // 为啥即使Matcher里面没有price，只要object设了，还会去match呢？一定要显式Ignore？
.name("espresso")
.build();
Optional<Coffee> result =
coffeeMongodbRepository.findOne(Example.of(criteria, matcher));

//根据ID删除
coffeeMongodbRepository.deleteById(row.getId())
```
### 使用 MongoTemplate
注入 `@Autowired MongoTemplate mongoTemplate;`

```java
//使用 Query对象进行查找，删除
mongoTemplate.remove(Query.query(Criteria.where("name").is("espresso")), Coffee.class);

Coffee c = mongoTemplate.findOne(Query.query(Criteria.where("name").is("espresso")), Coffee.class);
        
// 保存
mongoTemplate.save(espresso); 
```

## 遇到的问题
>Caused by: org.springframework.data.mapping.MappingException: Couldn't find PersistentEntity for type class java.lang.Object!

这个原因是在extend MongoRepository的时候没有写明类型 `extends MongoRepository<Coffee, String>` Coffee 和 String忘写了。


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
