package com.bry.coffeeshopmybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bry.coffeeshopmybatis.model.Coffee;

@Mapper
public interface CoffeeMapper {
    @Insert("insert into t_coffee (name, price, create_time, update_time)"
            + "values (#{name}, #{price}, now(), now())")
    @Options(useGeneratedKeys = true)
    int save(Coffee coffee);

    @Update("update t_coffee set name=#{name}, price=#{price}, update_time=now() where id=#{id}")
    int update(Coffee coffee);

    @Select("select * from t_coffee where id =#{id}")
    /*@Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
            // map-underscore-to-camel-case = true 可以实现一样的效果
            // @Result(column = "update_time", property = "updateTime"),
    })*/
    Coffee findById(Integer id);

    @Select("select * from t_coffee where name =#{name}")
    Coffee findByName(String name);

    /**
     * 错误：
     * #{id} 写成了 {#id}
     * 没有加上 @Param("id")
     *
     * @Param参数在有些情况下可以不加，比如名字能够匹配到，数量能够匹配到，不过为了表意明确，建议都加上
     */

    @Select("select * from t_coffee")
    List<Coffee> findAll();

    @Select("delete from t_coffee where name = #{name}")
    void deleteByName(@Param("name") String name);

    @Select(("select count(*) from t_coffee"))
    int count();

}
