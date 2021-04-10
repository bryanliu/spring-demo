package com.bry.nosqldemo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bry.nosqldemo.model.Coffee;

@SpringBootTest
public class CoffeeRepoMongoTemplate {

    @Autowired MongoTemplate mongoTemplate;

    @BeforeEach
    void precheckandinitial() {
        mongoTemplate.remove(Query.query(Criteria.where("name").is("espresso")), Coffee.class);
        mongoTemplate.remove(Query.query(Criteria.where("name").is("latte")), Coffee.class);

    }

    @AfterEach
    void aftercheckandcleanup() {
        mongoTemplate.remove(Query.query(Criteria.where("name").is("espresso")), Coffee.class);
        mongoTemplate.remove(Query.query(Criteria.where("name").is("latte")), Coffee.class);
    }

    @Test
    public void testSave(){
        Coffee espresso =
                Coffee.builder().name("espresso").price(200).createTime(new Date()).updateTime((new Date())).build();
        Coffee latte =
                Coffee.builder().name("latte").price(300).createTime(new Date()).updateTime((new Date())).build();
        mongoTemplate.save(espresso);
        Coffee c = mongoTemplate.findOne(Query.query(Criteria.where("name").is("espresso")), Coffee.class);
        assertNotNull(c);
        List<Coffee> l = mongoTemplate.find(Query.query(Criteria.where("name").is("espresso")), Coffee.class);
        assertEquals(1, l.size());
        assertEquals(espresso.getPrice(), c.getPrice());

    }

}
