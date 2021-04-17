# 这个项目演示了autoconfig.
演示的场景是 如果 在项目中有了特定的Bean，就不自动配置，没有的话就自动配置。

所以，为了演示这个效果，涉及到三个项目，
* hello 项目，目标Bean，
* autoconfig 项目，autoconfig的相关配置
* demo 演示项目

## 先来看hello项目
这个项目没什么，就是建了一个类 `Greeting`，实现了`ApplicationRunner`，没什么特别目的，主要是用来演示生成`Bean`的时候会跑一下`run`方法

## autoconfig 项目
这个项目是自动配置的核心
### 配置Pom
pom 里面加上 `spring-boot-autoconfigure`
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-autoconfigure</artifactId>
		</dependency>
```
并且依赖hello项目，scope 设 Provided 表示运行的时候上下文会提供，这边依赖作为测试和编译。
```xml
		<dependency>
			<groupId>com.bryan</groupId>
			<artifactId>autoconfig-hello</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<scope>provided</scope>
		</dependency>
```
### 创建autoconfigure 类
配置 autconfigure 的config类。  
```java
@Configuration
@ConditionalOnClass(Greeting.class)
public class GreetingAutoConfig {

    @Bean
    @ConditionalOnMissingBean(Greeting.class)
    @ConditionalOnProperty(name = "greeting.enable", havingValue = "true"
            , matchIfMissing = true)
    public Greeting getGreeting() {
        return new Greeting("from autoconfig");
    }
}
```
看这些Conditional应该能看明白分别是干嘛的。
* `@ConditionalOnClass` 只有在classpath中出现目标类，才做自动配置
* `@ConditionalOnMissingBean` 没有目标Bean 才做自动配置
* `@ConditionalOnProperty` 有特定的配置才做自动配置，`matchIfMissing` 没有也认为找到了，可以做自动配置

## demo 项目
这个项目索要做的就是把两个项目导入
```xml
		<dependency>
			<groupId>com.bryan</groupId>
			<artifactId>autoconfig-hello</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
		<dependency>
			<groupId>com.bryan</groupId>
			<artifactId>autoconfig-autoconfig</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
```
直接跑一下，就会有效果了。

当然，可以在这个项目里手动创建一个`Bean`，见下面代码，按照我们之前的配置，就不会用自动配置了。
```java
    @Bean
	public Greeting greeting(){
		return new Greeting("from created bean in appplication");
	}
```
