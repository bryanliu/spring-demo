这个项目演示了在Spring-boot 中如何使用MyBatis 并且用MyBatisGenerator生成相应资源

#准备
首先到https://start.spring.io/ 生成一个模板，选择
* Mybatis Framework，
* H2 - 开发环境用内存数据库，测试起来非常快
* Web，
* LomBok（用了之后可以少些很多setter，getter）

下载模板，导入到 IDEA中

检查一下 `pom.xml`
应该有如下内容，注意手工加上 Mybatis Generator
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
    <dependency>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-core</artifactId>
        <version>1.3.7</version>
    </dependency>
</dependencies>
```

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
logging.level.com.bry.coffeeshopmybatisgenerator.mapper=debug
```
>如果要打开SQL日志输出的话，参考上面设置。

## 生成模型
我们选择用MyBatist Generator 生成模型和Mapper
生成方式可以有代码的方式，Maven 的方式，我们选择代码的方式
### generatorConfig 配置文件
参考下面配置。
>注意1: 这边有个坑就是路径，如果项目是一个大项目的子模块，路径应该以子模块名开头，比如
>> coffeeshopmybatisgenerator/src/main/java

> 注意 2：我们选的生成方式是 MIXEDMAPPER，也就是生成接口和xml，简单的查询直接在接口里面实现就好了。

> 注意 3：生成的文件选择放到`auto`路径下，和手动的区分开，这样也不会覆盖手动的内容
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="H2Tables" targetRuntime="MyBatis3">
        <plugin type="org.mybatis.generator.plugins.FluentBuilderMethodsPlugin" />
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin" />
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin" />
        <plugin type="org.mybatis.generator.plugins.RowBoundsPlugin" />

        <jdbcConnection driverClass="org.h2.Driver"
                        connectionURL="jdbc:h2:mem:testdb"
                        userId="sa"
                        password="">
        </jdbcConnection>

        <javaModelGenerator targetPackage="com.bry.coffeeshopmybatisgenerator.model.auto"
                            targetProject="coffeeshopmybatisgenerator/src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <sqlMapGenerator targetPackage="com.bry.coffeeshopmybatisgenerator.mapper.auto"
                         targetProject="coffeeshopmybatisgenerator/src/main/resources/mapper/auto">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <javaClientGenerator type="MIXEDMAPPER"
                             targetPackage="com.bry.coffeeshopmybatisgenerator.mapper.auto"
                             targetProject="coffeeshopmybatisgenerator/src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>

        <table tableName="t_coffee" domainObjectName="Coffee" >
            <generatedKey column="id" sqlStatement="CALL IDENTITY()" identity="true" />
        </table>
    </context>
</generatorConfiguration>
```

## 测试
写了测试，一跑，失败了，`Error： : Result Maps collection does not contain value for
java.lang.IllegalArgumentException: Result Maps collection does not contain value for com.bry.coffeeshopmybatisgenerator.mapper.auto.CoffeeMapper.BaseResultMap`
原因是生成的Mapper不能被扫描到，所以在配置文件中加上：
```properties
mybatis.mapper-locations=classpath*:/mapper/**/*.xml
```
这样就可以了。  
测试就是直接调用Mapper的方法进行测试，详细请参考
[CoffeeMapperTest](src/test/java/com/bry/coffeeshopmybatisgenerator/mapper/auto/CoffeeMapperTest.java) 包括example的使用，里面也有

## 手工扩展自动生成的Mapper
具体例子参考 [CoffeeRepo](src/main/java/com/bry/coffeeshopmybatisgenerator/mapper/CoffeeRepo.java)， xml配置 [CoffeeRepo.xml](src/main/resources/mapper/auto/com/bry/coffeeshopmybatisgenerator/mapper/CoffeeRepo.xml) 
以及测试[CoffeeRepoTest](src/test/java/com/bry/coffeeshopmybatisgenerator/mapper/CoffeeRepoTest.java)

注意看XML里面的`resultMap` 和 `include refid` 都是复用自动生成的类型
# PageHelper的支持
加上依赖
```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.10</version>
</dependency>
```
然后就可以生成一个带参数的方法，具体参考
```java
@Select("select * from t_coffee")
public List<Coffee> findAllwithParam(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
```

```java
    @Test
    public void testPaginationWithParam() {
        List<Coffee> coffees = coffeeRepo.findAllwithParam(2, 2);
        assertEquals(2, coffees.size());
        assertEquals("capuccino", coffees.get(0).getName());
        PageInfo page = new PageInfo(coffees);
        log.info("{}", page); // PageInfo{pageNum=2, pageSize=2, size=2, startRow=3, endRow=4, total=5, pages=3,
        assertEquals(2, page.getPageNum());
        assertEquals(2, page.getPageSize());
        assertEquals(3, page.getStartRow());
        assertEquals(4, page.getEndRow());
        assertEquals(5, page.getTotal());
        assertEquals(3, page.getPages());

        coffees = coffeeRepo.findAllwithParam(-2, 2);
        log.info("{}", coffees);

    }
```