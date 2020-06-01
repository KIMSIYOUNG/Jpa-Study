package com.springdata.han.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springdata.han.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByName(String name);

}
