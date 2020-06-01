package com.springdata.han.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.springdata.han.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void create() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);
        List<Member> members = memberJpaRepository.findByUsername("member1");
        assertThat(members).hasSize(1);
    }
}