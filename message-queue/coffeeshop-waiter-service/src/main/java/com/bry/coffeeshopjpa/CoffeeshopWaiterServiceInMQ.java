package com.bry.coffeeshopjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bry.coffeeshopjpa.support.Barista;

@SpringBootApplication
//@EnableJpaRepositories
@EnableTransactionManagement
@EnableCaching(proxyTargetClass = true)
//@EnableAspectJAutoProxy // 可以不开启，Spring Boot 做自动配置了。
@EnableDiscoveryClient
@EnableBinding(Barista.class)
public class CoffeeshopWaiterServiceInMQ {

    public static void main(String[] args) {

        SpringApplication.run(CoffeeshopWaiterServiceInMQ.class, args);
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
