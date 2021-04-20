package com.example.eurekaserver.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoffeeOrder {

    Integer id;

    String customer;

    List<Coffee> items;

    String state;

    Date createTime;

    Date updateTime;
}
