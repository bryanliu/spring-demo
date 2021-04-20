# 这个项目演示了在Spring-boot 中如何使用JPA

首先到https://start.spring.io/ 生成一个模板，选择 
* Spring Data JPA，
* H2 - 开发环境用内存数据库，测试起来非常快
* Web，
* LomBok（用了之后可以少些很多setter，getter）

下载模板，导入到 IDEA中

检查一下 `pom.xml`
应该有如下内容：
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
```
接下来我们快速做一个模型的 CRUD，这里我们选择Coffee作为模型。

## 生成数据库表
我们选择不用JPA生成数据库表，因为这个在实际开发过程中不会这么用。都是写好脚本执行的。
所以我们将数据库初始化脚本放到 `schema.sql` 中。
简单的几个表，而且偷懒将数据初始化也放到这个里面，标准的话可以放到`data.sql`中
所以我们要做几个配置。

注：默认的脚本名字就是`schema.sql` 和 `data.sql` 如果想用自定义的名字，可以在配置文件中设置
```properties
# 不用JPA自动生成数据表
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true

#只是在内存数据库的时候初始化
spring.datasource.initialization-mode=embedded
```
## 生成模型
```java
@Entity
@Table(name = "T_COFFEE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Coffee implements Serializable {

    private String name;

    private Integer price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false)
    @UpdateTimestamp
    private Date updateTime;

    @Column
    @CreationTimestamp
    private Date createTime;
}

```
很简单的一个模型，包括id, name, price, updatetime, createtime
注意这里的标记有两大类。
* 一类是 `JPA`相关的，包括 `@Entity` `@Table(name="table_name")` `ID` `GeneratedValue`
`@Column`(默认其实不用写) `@CreationTimestamp`
* 一类是 `lombok` 相关的 
  * `@Data` 表示这是一个Model，会自动生成seter, getter。当然要在IDEA中装IDEA插件
  * `@NoArgsConstructor` 和 `@AllArgsConstructor` 生成*没参数*和*所有参数*的构造函数 
  * `@Builder` 生成builder访问方式，可以 `Coffee.builder().name("a").price(10).build()` 来生成对象
  * `@ToString(callSuper = true)` 如果有父类，则`toString` 要包括父类
    
## 构建Repository 接口（也就是我们理解的DAO，现在都叫Repository）
扩展`extends JpaRepository<Model, Primarykey类型>` 就好了。
一个方法声明都不用写，大部分CRUD场景已经能够满足了，很厉害!

比如
```java
// 保存，更新
Coffee c = coffeeRepository.save(coffee);
// 查找
Coffee c = coffeeRepository.findById(<id>);
// 删除
coffeeRepository.deleteById(res.getId());

//可以用Exampe的方式定义查询条件
ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact());
Optional<Coffee> result = coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(), matcher));
```
当然也可以自定义有语义的方法甚至写SQL

<details>
<summary>CODE</summary>

```java
    //根据名字查找，你不需要写实现，JPA自动会根据名字解析
    Coffee findByName(String name);

// 写SQL查询
@Query(
        value = "select coffee from Coffee coffee order by coffee.price desc"
)
    List<Coffee> listAllCoffees();

        void deleteByName(String name);
```
</details>

## 单元测试
非常方便吧，如果是简单的场景，也可以考虑一下JPA。生产力满满。
到这里，一个模型的CRUD就完成了。
具体测试查看 [CoffeeRepositoryTest.java](src/test/java/com/bry/coffeeshopjpa/repository/CoffeeRepositoryTest.java)

## 其他？
这边只是完成了一个基本数据层的实现，包括 `repository` 和 `model`层。

其他数据处理的扩展点包括分页查询，关联查询等，作为**TODO**吧

至于`Service` `Controller` 就可以根据具体需求扩展拉。

# 如何使用AOP给方法做切面
## 声明一个切面类，
要在里面配置切入点PointCut是什么，Advice是什么，之前，之后还是around  
这个类要标注为`@Aspect` 并且声明为 `@Component`
```java
@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();
        String name = pjp.getSignature().toString();
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Execute {}, time {}", name, endTime - startTime);
        }

    }

    @Pointcut("execution(* com.bry.coffeeshopjpa.repository..*(..))")
    private void repositoryOps() {

    }
}
```
也可以这么定义，表示对于以`testBean` 开头的做切面，切面是返回后执行
```java
@Aspect
@Slf4j
public class TestAspect {

    @AfterReturning("bean(testBean*)")
    public void printAfter() {
        log.info("After hello()");
    }
}

```

然后在 `SpringApplication类上面加上` `@EnableAspectJAutoProxy` 可以声明，Spring Boot 做自动配置了。但是作为一个好的习惯，还是要显式写一下。

> 说明： @Aspect 只是沿用Aspectj 的风格，但是后面实现还是AOP，不会引入AspectJ的complier或weaver，说明见[这里](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-ataspectj)

>更多表达式内容，参考[Spring 文档](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-pointcuts-examples)  
>> 以下面表达式为例：   
>>@Pointcut("execution(* geektime.spring.springbucks.repository..*(..))”)  
>>语法比较复杂，一般是 `returntype` `名称` `参数 `这三个必选
名称是可以省略的。  
>> \* 代表所有  
>>  .. 代表子路径  
>>  (..) 代表任意多个参数  
>>  所以上面的意思是repo 文件夹以及子路径中的所有方法，方法返回参数任意，方法参数任意。
