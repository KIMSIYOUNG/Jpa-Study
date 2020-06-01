package com.springdata.han;

import static com.querydsl.jpa.JPAExpressions.*;
import static com.springdata.han.QMember.*;
import static com.springdata.han.QTeam.*;
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

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
@Transactional
public class QueryDslTest {
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
    void name() {
        final Member result = em.createQuery("select m from Member m where m.name=:name", Member.class)
            .setParameter("name", "member1")
            .getSingleResult();

        assertThat(result.getAge()).isEqualTo(20);
    }

    @Test
    void queryDsl() {
        final Member findMember = queryFactory
            .select(member)
            .from(member)
            .where(member.name.eq("member1"))
            .fetchOne();
        assertThat(findMember.getAge()).isEqualTo(20);
    }

    @Test
    void search() {
        final Member member = queryFactory
            .selectFrom(QMember.member)
            .where(QMember.member.name.eq("member1")
                .and(QMember.member.age.between(10, 30)))
            .fetchOne();
        assertThat(member.getAge()).isEqualTo(20);
    }

    @Test
    void searchAndParam() {
        final Member findMember = queryFactory
            .selectFrom(member)
            .where(
                member.name.eq("member1"),
                member.age.eq(20)
            )
            .fetchOne();
        assertThat(findMember.getAge()).isEqualTo(20);
    }

    @Test
    void resultFetch() {
        final List<Member> fetch = queryFactory
            .selectFrom(member)
            .fetch();

        final Member fetchFirst = queryFactory
            .selectFrom(member)
            .fetchFirst();

        final QueryResults<Member> results = queryFactory
            .selectFrom(member)
            .fetchResults();

        final List<Member> members = results.getResults();
        final long total = results.getTotal();

        final long count = queryFactory
            .selectFrom(member)
            .fetchCount();
    }

    @Test
    void sort() {
        final List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(100))
            .orderBy(member.age.desc(), member.name.asc().nullsLast())
            .fetch();
        final Member member5 = result.get(0);
        final Member member6 = result.get(1);
        final Member memberNull = result.get(2);

        assertThat(member5.getName()).isEqualTo("member5");
        assertThat(member6.getName()).isEqualTo("member6");
        assertThat(memberNull.getName()).isNull();
    }

    @Test
    void paging() {
        List<Member> fetch = queryFactory
            .selectFrom(member)
            .orderBy(member.name.desc())
            .offset(1)
            .limit(2)
            .fetch();

        assertThat(fetch).hasSize(2);
    }

    @Test
    void aggregation() {
        final List<Tuple> result = queryFactory
            .select(
                member.count(),
                member.age.sum(),
                member.age.avg()
            )
            .from(member)
            .fetch();
        final Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
    }

    @Test
    void groupBy() {
        final List<Tuple> result = queryFactory
            .select(team.name, member.age.avg())
            .from(member)
            .join(member.team, team)
            .groupBy(team.name)
            .fetch();
        final Tuple teamA = result.get(0);
        final Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
    }

    @Test
    void join() {
        List<Member> result = queryFactory
            .selectFrom(member)
            .join(member.team, team)
            .where(team.name.eq("teamA"))
            .fetch();

        assertThat(result)
            .extracting("name")
            .containsExactly("member1", "member2");
    }

    @Test
    void on() {
        List<Tuple> result = queryFactory
            .select(member, team)
            .from(member)
            .leftJoin(member.team, team)
            .on(team.name.eq("teamA"))
            .fetch();

        List<Tuple> innerResult = queryFactory
            .select(member, team)
            .from(member)
            .join(member.team, team)
            .where(team.name.eq("teamA"))
            .fetch();

        for (Tuple tuple : result) {
            System.out.println(tuple);
        }

        for (Tuple tuple : innerResult) {
            System.out.println(tuple);
        }
    }

    @Test
    void withoutRelation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
            .select(member, team)
            .from(member)
            .leftJoin(team).on(member.name.eq(team.name))
            .fetch();
        result.forEach(System.out::println);
    }

    @Test
    void fetchJoin() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
            .selectFrom(member)
            .where(member.name.eq("member1"))
            .fetchOne();

        boolean result = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(result).isFalse();
    }

    @Test
    void fetchJoinWithFetch() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
            .selectFrom(member)
            .join(member.team, team).fetchJoin()
            .where(member.name.eq("member1"))
            .fetchOne();

        boolean result = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(result).isTrue();
    }

    @Test
    void subQuery() {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(
                select(memberSub.age.max())
                    .from(memberSub)
            ))
            .fetch();

        for (Member member5 : result) {
            System.out.println(member5);
        }
    }

    @Test
    void subQuery2() {
        QMember memberSub = new QMember("memberSub");
        List<Tuple> result = queryFactory
            .select(member.name,
                select(memberSub.age.avg())
                    .from(memberSub)
            )
            .from(member)
            .fetch();
        for (Tuple tuple : result) {
            System.out.println(tuple);
        }
    }

    @Test
    void caseWhen() {
        List<String> result = queryFactory
            .select(member.age
                .when(10).then("어린이")
                .when(20).then("older")
                .otherwise("else")
            )
            .from(member)
            .fetch();
        result.forEach(System.out::println);
    }

    @Test
    void caseWhenComplex() {
        List<String> result = queryFactory
            .select(new CaseBuilder()
                .when(member.age.between(0, 20)).then("AAA")
                .when(member.age.between(20, 30)).then("BBB")
                .otherwise("CCC")
            )
            .from(member)
            .fetch();
        result.forEach(System.out::println);
    }

    @Test
    void plusConst() {
        List<Tuple> result = queryFactory
            .select(member.name, Expressions.constant("A"))
            .from(member)
            .fetch();
        result.forEach(System.out::println);

        String member = queryFactory
            .select(QMember.member.name.concat("_").concat(QMember.member.age.stringValue()))
            .from(QMember.member)
            .where(QMember.member.name.eq("member1"))
            .fetchOne();
        assertThat(member).isEqualTo("member1_20");
    }
}

