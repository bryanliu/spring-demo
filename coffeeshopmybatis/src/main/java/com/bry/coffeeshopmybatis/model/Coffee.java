package com.bry.coffeeshopmybatis.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
