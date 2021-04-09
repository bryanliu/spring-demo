package com.bry.coffeeshopmybatisgenerator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bry.coffeeshopmybatisgenerator.mapper.auto.CoffeeMapper;
import com.bry.coffeeshopmybatisgenerator.model.auto.Coffee;

@Mapper
public interface CoffeeRepo extends CoffeeMapper {

    @Select("select * from t_coffee where name = #{name}")
    public Coffee searchByName(String name);

    public Coffee searchByNameinXMl(String name);
}
