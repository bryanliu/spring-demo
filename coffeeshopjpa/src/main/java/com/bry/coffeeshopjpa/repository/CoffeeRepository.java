package com.bry.coffeeshopjpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bry.coffeeshopjpa.model.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {

    Coffee findByName(String name);

    @Query(
            value = "select coffee from Coffee coffee order by coffee.price desc"
    )
    List<Coffee> listAllCoffees();

    /**
     * 这个 NameIn 中的 In 很关键，有了它就变成了IN查找了。
     *
     * @param names
     * @return
     */
    List<Coffee> findCoffeeByNameInOrderById(List<String> names);

    void deleteByName(String name);
}
