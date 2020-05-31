package com.springdata.han;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
class HanApplicationTests {
    @PersistenceContext
    private EntityManager em;

    @Test
    void contextLoads() {
        
    }

}
