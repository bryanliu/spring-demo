package com.bry.coffeeshopmybatisgenerator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.bry.coffeeshopmybatisgenerator.mapper.auto.CoffeeMapper;
import com.bry.coffeeshopmybatisgenerator.model.auto.Coffee;

@Mapper
public interface CoffeeRepo extends CoffeeMapper {

    @Select("select * from t_coffee where name = #{name}")
    public Coffee searchByName(String name);

    public Coffee searchByNameinXMl(String name);

    @Select("select * from t_coffee")
    public List<Coffee> findAllwithParam(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
