package com.bry.coffeeshopjpa;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.client.ConfigServerInstanceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableJpaRepositories
@EnableTransactionManagement
@EnableCaching(proxyTargetClass = true)
//@EnableAspectJAutoProxy // 可以不开启，Spring Boot 做自动配置了。
@EnableDiscoveryClient
public class CoffeeshopWaiterService {

    public static void main(String[] args) {

        SpringApplication.run(CoffeeshopWaiterService.class, args);
    }

//    @Bean
//    public ConfigServerInstanceProvider configServerInstanceProvider(
//            ObjectProvider<ConfigServerInstanceProvider.Function> function,
//            ObjectProvider<DiscoveryClient> discoveryClient) {
//        ConfigServerInstanceProvider.Function fn = function.getIfAvailable();
//        if (fn != null) {
//            return new ConfigServerInstanceProvider(fn);
//        }
//        DiscoveryClient client = discoveryClient.getIfAvailable();
//        if (client == null) {
//            throw new IllegalStateException("ConfigServerInstanceProvider requires a DiscoveryClient or Function");
//        }
//        return new ConfigServerInstanceProvider(client::getInstances);
//    }

}
