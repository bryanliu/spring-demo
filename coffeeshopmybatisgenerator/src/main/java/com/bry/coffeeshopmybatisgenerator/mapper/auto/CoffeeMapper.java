package com.bry.coffeeshopmybatisgenerator.mapper.auto;

import com.bry.coffeeshopmybatisgenerator.model.auto.Coffee;
import com.bry.coffeeshopmybatisgenerator.model.auto.CoffeeExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface CoffeeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    long countByExample(CoffeeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    int deleteByExample(CoffeeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    @Delete({
        "delete from T_COFFEE",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    @Insert({
        "insert into T_COFFEE (NAME, PRICE, ",
        "CREATE_TIME, UPDATE_TIME)",
        "values (#{name,jdbcType=VARCHAR}, #{price,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="CALL IDENTITY()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Coffee record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    int insertSelective(Coffee record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    List<Coffee> selectByExampleWithRowbounds(CoffeeExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    List<Coffee> selectByExample(CoffeeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    @Select({
        "select",
        "ID, NAME, PRICE, CREATE_TIME, UPDATE_TIME",
        "from T_COFFEE",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("com.bry.coffeeshopmybatisgenerator.mapper.auto.CoffeeMapper.BaseResultMap")
    Coffee selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    int updateByExampleSelective(@Param("record") Coffee record, @Param("example") CoffeeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    int updateByExample(@Param("record") Coffee record, @Param("example") CoffeeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    int updateByPrimaryKeySelective(Coffee record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_COFFEE
     *
     * @mbg.generated Fri Apr 09 21:49:52 CST 2021
     */
    @Update({
        "update T_COFFEE",
        "set NAME = #{name,jdbcType=VARCHAR},",
          "PRICE = #{price,jdbcType=INTEGER},",
          "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
          "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP}",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Coffee record);
}