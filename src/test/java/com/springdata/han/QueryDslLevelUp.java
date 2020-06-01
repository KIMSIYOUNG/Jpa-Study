package com.springdata.han;

import static com.springdata.han.QMember.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
@Transactional
public class QueryDslLevelUp {
    private Team team1;
    private Team team2;
    private Member member1;
    private Member member2;
    private Member member3;
    private Member member4;

    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    EntityManagerFactory emf;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);
        team1 = new Team("teamA");
        team2 = new Team("teamB");
        em.persist(team1);
        em.persist(team2);

        member1 = new Member("member1", 20, team1);
        member2 = new Member("member2", 30, team1);
        member3 = new Member("member3", 40, team2);
        member4 = new Member("member4", 50, team2);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));
    }

    @Test
    void simpleProjection() {
        List<Tuple> fetch = queryFactory
            .select(member.name, member.age)
            .from(member)
            .fetch();
        for (Tuple tuple : fetch) {
            System.out.println(tuple);
            System.out.println(tuple.get(member.name));
            System.out.println(tuple.get(member.age));
        }
    }

    @Test
    void projectionDto() {
        List<Tuple> members = queryFactory
            .select(Projections.constructor(MemberDto.class),
                member.name,
                member.age)
            .from(member)
            .fetch();
        for (Tuple tuple : members) {
            System.out.println(tuple);
        }
    }

    @Test
    void DynamicQueryBooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameParam, Integer ageParam) {
        BooleanBuilder builder = new BooleanBuilder();
        if (usernameParam != null) {
            builder.and(member.name.eq(usernameParam));
        }
        if (ageParam != null) {
            builder.and(member.age.eq(ageParam));
        }

        return queryFactory
            .selectFrom(member)
            .where(builder)
            .fetch();
    }

    @Test
    void DynamicWithWhere() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameParam, Integer ageParam) {
        return queryFactory
            .selectFrom(member)
            .where(nameEq(usernameParam), ageEq(ageParam))
            .fetch();
    }

    private Predicate ageEq(Integer ageParam) {
        return ageParam != null ? member.age.eq(ageParam) : null;
    }

    private Predicate nameEq(String usernameParam) {
        return usernameParam != null ? member.name.eq(usernameParam) : null;
    }

    @Test
    void bulkAdd() {
        long result = queryFactory
            .update(member)
            .set(member.age, member.age.add(1))
            .execute();

        long count = queryFactory
            .select(member.name.count())
            .from(member)
            .fetchCount();
        assertThat(result).isEqualTo(count);
    }

    @Test
    void name() {
        queryFactory
            .delete(member)
            .where(member.age.gt(18))
            .execute();
    }

    @Test
    void bulk() {
        long count = queryFactory
            .update(member)
            .set(member.name, "비회원")
            .where(member.age.lt(28))
            .execute();

        em.flush();
        em.clear();

        List<Member> result = queryFactory
            .selectFrom(member)
            .fetch();
        for (Member member5 : result) {
            System.out.println("member5 = " + member5);
        }
    }
}
