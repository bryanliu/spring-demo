package com.example.eurekaserver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.example.eurekaserver.support.CustomConnectionKeepAliveStrategy;

@SpringBootApplication
@EnableDiscoveryClient
public class CoffeeshopCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeshopCustomerApplication.class, args);
    }

//    @Bean
//    public HttpComponentsClientHttpRequestFactory requestFactory() {
//        PoolingHttpClientConnectionManager connectionManager =
//                new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
//        connectionManager.setMaxTotal(200);
//        connectionManager.setDefaultMaxPerRoute(20);
//
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(connectionManager)
//                .evictIdleConnections(30, TimeUnit.SECONDS)
//                .disableAutomaticRetries()
//                // 有 Keep-Alive 认里面的值，没有的话永久有效
//                //.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
//                // 换成自定义的
//                .setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
//                .build();
//
//        HttpComponentsClientHttpRequestFactory requestFactory =
//                new HttpComponentsClientHttpRequestFactory(httpClient);
//
//        return requestFactory;
//    }

    @Bean
    public HttpComponentsClientHttpRequestFactory requestFactory() {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return requestFactory;
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(100))
                .setReadTimeout(Duration.ofMillis(500))
                .requestFactory(this::requestFactory)
                .build();
    }
}
