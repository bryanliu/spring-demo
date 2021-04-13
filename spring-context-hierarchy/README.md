# Context 的继承关系以及AOP的作用范围

这个例子做了以下练习：
* 两种方式手动加载`context`，一种是`Config`类，一种是`XML`
* `context` 有继承关系
* 两个`context`都有相同名字的Bean
* 分别在两个`Context`做`AOP`的效果

目前的bean以及对应的Context
* beanA in root context  
* beanB in root context 
* beanB in sub context

## Case 1：root 开启AOP，Sub 没有开启AOP
* 调用 `beanA in root context` -- 增强
* 调用 `beanB in root context` -- 增强
* 调用 `beanB in sub context` -- 没有增强
    * 因为先从`sub context` 找`beanB`, 找到就不再从`root context` 中找了，sub context没开启AOP，所以不会增强。

## Case 2：root 开启AOP，Sub 开启AOP
* 调用 `beanA in root context` -- 增强
* 调用 `beanB in root context` -- 增强
* 调用 `beanB in sub context` -- 增强
>这个很好理解

## Case 3：root 没有开启AOP，Sub 开启AOP
* 调用 `beanA in root context` -- 没有增强
* 调用 `beanB in root context` -- 没有增强
* 调用 `beanB in sub context` -- 增强

### 这个还有一个子Case
* 切面配置在父Context，没配置在sub context，还是可以增强，因为会从父Context中加载切面。
* 切面只配置在子Context，当然是能够增强的。
* 如果切面配置在子context，但是AOP开启在父Context，就一个都不能增强了。

通过这些Case要理解Context的继承关系以及，AOP的作用范围。

**总结一下：**
* bean 从当前Context一层层往上找
* AOP只作用在当前Context

## 附：
### Config的AOP的配置方式，
* `@EnableAspectJAutoProxy` 开启AOP
* Aspect 类声明为一个Bean (也可以从Component声明为一个Bean)
```java
@EnableAspectJAutoProxy
public class RootConfig {


    @Bean
    public FooAspect fooAspect(){
        return new FooAspect();
    }
    

```
```java
//Aspect 定义
//注意@Aspect 标注
@Aspect
@Slf4j
public class FooAspect {

    @AfterReturning("bean(testBean*)")
    public void printAfter() {
        log.info("After hello()");
    }
    }
```

### XML的AOP的配置方式
* 第一行是开启AOP
* 第二行是配置切面

```xml

<aop:aspectj-autoproxy/>


<bean id="fooAspect" class="com.example.context.TestAspect"/>
```

### 从COnfig 和 XML 分别创建Context
```java
ApplicationContext rootContext = new AnnotationConfigApplicationContext(RootConfig.class);
// 从xml创建context，并把rootContext 加进去做为父Context
ClassPathXmlApplicationContext subContext = new ClassPathXmlApplicationContext(
        new String[]{"applicationContext.xml"}, rootContext
);
```