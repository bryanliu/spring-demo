package com.bry.coffeeshopjpa.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_order")
public class CoffeeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String customer;

    //@ManyToMany(fetch = FetchType.EAGER)
    @ManyToMany()
    @JoinTable(name = "t_coffee_order")
    @OrderBy("id")
    List<Coffee> items;

    OrderState state;

    @CreationTimestamp
    Date createTime;

    @UpdateTimestamp
    Date updateTime;
}
