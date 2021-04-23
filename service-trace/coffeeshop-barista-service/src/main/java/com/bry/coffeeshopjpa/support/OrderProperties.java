package com.bry.coffeeshopjpa.support;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("coffee")
@RefreshScope
@Data
public class OrderProperties {

    Integer discount = 100;
    String prefix = "springbucks-";

}
