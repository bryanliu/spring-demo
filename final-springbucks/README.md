# 最终版本
在最终版本中，我们基于之前开发的三个服务，我们将要将这三个服务打包成Docker  
并且制作一个`Docker-Composer` 将三个服务以及所有的其他依赖都启动起来

这个Demo完成
 * 增加 Docker Build Plugin
   * maven package 的时候就会打包
 * 添加dev环境的properties，将dev相关的配置放到这里面，比如服务的地址 
 * Dockerfile
    * 把基础进项换为 `java:8-jdk-alpine`，这个比 `java:8` 打出来的包小好多。前者要800M，后者只要200M。但是其实我的Jar包才60M
 * Docker-Compose 文件
   * 暂时没用到 `mysql` 和 `Redis`，先注掉了
   * 要用 `docker-compose up -d —build` 会更新镜像，不然不会更新最新的镜像或配置
   * `docker compose` 的`links`的使用， 相当于搭建了一个内部网络，通过容器名就可以作为IP访问了。
 * .env 文件，docker-compose 会引用里面的变量，
   * 比如 这儿就把`SPRING_PROFILES_ACTIVE`设为`dev`,这样`docker-compose`启动的时候就会把Profile设为dev.

> 注意由于`Config Service` 配置加载的问题，不能从`application-dev.properties`里面读取 consul的地址，还是从`application.properties`里面加载`localhost`，所以读不到了，现在先把远程配置这一块关掉了。  
> 
> 原因是由于`docker-compose` 用的是`links` link到 `consul`, 所以在配置文件里面把`consul地址`改成了consul容器的名字，结果还是读到localhost, 所以连接被拒绝了。
> 
>> Config data resource '[ConsulConfigDataResource@3979c6e8 context = 'config/barista-service,dev/', optional = true, properties = [ConsulConfigProperties@6fb3d3bb enabled = true, prefixes = list['config'], defaultContext = 'application', profileSeparator = ',', format = YAML, dataKey = 'data', aclToken = [null], watch = [ConsulConfigProperties.Watch@38a3c078 waitTime = 55, enabled = true, delay = 1000], failFast = true, name = 'barista-service']]' via location 'consul:' does not exist
>>
>> Action:  
>> Check that the value 'consul:' is correct, or prefix it with 'optional:'
> 

## 启动
1. Terminal 进入到 docker-compose.yml 的目录  
2. 使用 `docker-compose up -d --build` 就可以启动所有的容器和中间件依赖了。