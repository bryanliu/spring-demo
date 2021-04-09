# 这个项目演示了在Spring-boot 中如何使用JPA

首先到https://start.spring.io/ 生成一个模板，选择
* Mybatis Framework，
* H2 - 开发环境用内存数据库，测试起来非常快
* Web，
* LomBok（用了之后可以少些很多setter，getter）

下载模板，导入到 IDEA中

检查一下 `pom.xml`
应该有如下内容：
```xml
	<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.1.4</version>
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
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
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
#只是在内存数据库的时候初始化
spring.datasource.initialization-mode=embedded
#在model字段和表字段映射的时候，将字段下划线形式自动转换成驼峰形式，
mybatis.configuration.map-underscore-to-camel-case=true
#显示SQL，注意路径是本项目mapper的路径
logging.level.com.bry.coffeeshopmybatis.mapper=debug
```
>如果要打开SQL日志输出的话，参考上面设置。

## 生成模型
这个Model只要写Lombok的注解了，简单很多。（但是你会看到这儿简单了，ORM的时候稍微麻烦点了）
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coffee {

    private Integer id;

    private String name;

    private Integer price;

    private Date createTime;

    private Date updateTime;
}

```
很简单的一个模型，包括id, name, price, updatetime, createtime
*  `lombok` 相关的Annotation
    * `@Data` 表示这是一个Model，会自动生成seter, getter。当然要在IDEA中装IDEA插件
    * `@NoArgsConstructor` 和 `@AllArgsConstructor` 生成*没参数*和*所有参数*的构造函数
    * `@Builder` 生成builder访问方式，可以 `Coffee.builder().name("a").price(10).build()` 来生成对象

## 构建Mapper 接口（也就是我们理解的DAO）
这儿就和JPA不同了，每个方法都的自己写SQL。不过对SQL的掌控就非常好了。
以下是例子。
```java
@Mapper
public interface CoffeeMapper {
    @Insert("insert into t_coffee (name, price, create_time, update_time)"
            + "values (#{name}, #{price}, now(), now())")
    @Options(useGeneratedKeys = true)
    int save(Coffee coffee);

    @Update("update t_coffee set name=#{name}, price=#{price}, update_time=now() where id=#{id}")
    int update(Coffee coffee);

    @Select("select * from t_coffee where id =#{id}")
    /*@Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
            // map-underscore-to-camel-case = true 可以实现一样的效果
            // @Result(column = "update_time", property = "updateTime"),
    })*/
    Coffee findById(Integer id);

    @Select("select * from t_coffee where name =#{name}")
    Coffee findByName(String name);
    
    @Select("select * from t_coffee")
    List<Coffee> findAll();

    @Select("delete from t_coffee where name = #{name}")
    void deleteByName(@Param("name") String name);

    @Select(("select count(*) from t_coffee"))
    int count();

}
```
注意：
* 在Interface级别加上`@Mapper`
* 在ApplicationClass 上标注要扫描的Mapper地址 `@MapperScan("com.bry.coffeeshopmybatis.mapper")`
  * 不过我目前没写，也可以正常工作



几个错误：
* `#{id}` 写成了 `{#id}`
* 没有加上 `@Param("id")` 
> 不过`@Param` 也不是必选的，有些规则可以匹配到，就不需要显式写了


## 单元测试
非常方便吧，如果是简单的场景，也可以考虑一下JPA。生产力满满。
到这里，一个模型的CRUD就完成了。  
具体测试查看 [CoffeeMapperTest.java](src/test/java/com/bry/coffeeshopmybatis/mapper/CoffeeMapperTest.java)

## 其他？
这边只是完成了一个基本数据层的实现，包括 `Mapper` 和 `model`层。

TODO：
* 分页查询，关联查询等，
* 自动生成Mybatis相关文件，并且和手工配置分开


至于`Service` `Controller` 就可以根据具体需求扩展拉。
