package study.querydsl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.EqualsAndHashCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@EqualsAndHashCode
@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void beforeEach() {

        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    }

    @Test
    public void startJPQL() throws Exception {

        //given
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        //when

        //then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQueryDsl() throws Exception {

        //given
//        QMember m = new QMember("m");
//        QMember m = QMember.member;  //이렇게도 활용 가능
        // static import 로도 코드 간결화 가능

        Member findMember = queryFactory.select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        //when

        //then

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search() throws Exception {

        //given
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        //when

        //then
        assertThat(findMember.getUsername()).isEqualTo("member1");
//        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test
    public void resultFetchTest() throws Exception {

        //list 조회
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        //단건조회
        Member fetchOne = queryFactory.selectFrom(member).fetchOne();

        // 1개 단건조회
        Member fetchFirst = queryFactory.selectFrom(member).fetchFirst(); // .limit(1).fetchOne()


        /**
         * fetchResults 는 deprecated되었다.
         * 원래 동작은 페이징 처리 + total count 쿼리 추가실행을 해주었으나,
         * 복잡한 쿼리에서는 동작이 잘 되지않아 deprecated 되었다고 함.
         *
         * fetchCount 역시 마찬가지
         * count쿼리로 변경해서 count 수를 조회했으나,
         * 역시 deprecated되었다
         */
//        QueryResults<Member> results = queryFactory.selectFrom(member).fetchResults();
//        long l = queryFactory.selectFrom(member).fetchCount();

        // count 쿼리를 새로 사용하는 방법
        Long totalCount = queryFactory
                //.select(Wildcard.count) //select count(*)
                .select(member.count()) //select count(member.id)
                .from(member)
                .fetchOne();
        System.out.println("totalCount = " + totalCount);


    }

    /**
     * 정렬 순서
     * 1. 회원 나이 내림차순 (desc)
     * 2. 회원 이름 오름차순 (asc). 단, 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    public void sort() throws Exception {

        //given
        em.persist(new Member(null, 100, null));
        em.persist(new Member("member5", 100, null));
        em.persist(new Member("member6", 100, null));

        //when
        List<Member> result = queryFactory.selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        //then
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    @Test
    public void paging1() throws Exception {

        //given
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        //when

        //then

        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void paging2() throws Exception {

        //given
        Long count = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        //when
        //then
        System.out.println("==========================================================");
        System.out.println("count = " + count);

    }

    /**
     * 집합
     */
    @Test
    public void aggregation() throws Exception {

        //given
        List<Tuple> result = queryFactory.select(
                        member.count(),
                        member.age.sum(),
                        member.age.max(),
                        member.age.min(),
                        member.age.avg()
                ).from(member)
                .fetch();

        //when

        //then

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라
     */
    @Test
    public void group() throws Exception {

        //given
        List<Tuple> result = queryFactory
                .select(team.name,
                        member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        //when
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        //then

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);



    }

    /**
     * 조인
     * 팀 A에 소속된 모든 회원
     */
    @Test
    public void join() throws Exception {

        //given
        List<Member> result = queryFactory.selectFrom(member)
                .join(member.team, team) //INNER JOIN member1.team AS team
                .where(team.name.eq("teamA"))
                .fetch();

        //when

        //then
        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");

    }

    /**
     * 세타 조인 ( 연관관계 없어도 조인이 가능)
     *
     * 주의점 : 외부 조인(left 등등)이 불가능
     */
    @Test
    public void theta_join() throws Exception {

        //given
        em.persist(new Member("teamA", 10, null));
        em.persist(new Member("teamB", 20, null));
        em.persist(new Member("teamC", 20, null));

        //when
        List<Member> result = queryFactory.select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();
        //then
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");

    }

    /**
     * Join - on
     *  1) 조인 대상 필터링
     *  2) 연관관계 없는 엔티티 외부 조인
     */

    /**
     * 1) 조인 대상 필터링
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     *      *  JPQL : select m, t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    public void joinOn() throws Exception {

        //given
        List<Tuple> result = queryFactory.select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA")) //left조인일 경우에만 의미있다. inner조인일 경우에는 그냥 where과 다를바가 없음.
                .fetch();
        //when
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    /**
     * 2) 연관관계 없는 엔티티 외부 조인
     *  회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    public void joinOn_NoRelation() throws Exception {

        //given
        em.persist(new Member("teamA", 10, null));
        em.persist(new Member("teamB", 20, null));
        em.persist(new Member("teamC", 20, null));

        //when
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();
        //then
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 페치 조인
     *
     */

    @PersistenceUnit
    EntityManagerFactory emf;


    @Test
    public void noneFetchJoin() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory.selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        //then
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();

    }

    @Test
    public void fetchJoin() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory.selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        //then
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();

    }

    /**
     * 서브 쿼리
     *
     * 1. 나이가 가장 많은 회원
     * 2. 나이가 평균인 회원
     * 3-1. IN 사용하는 방법(where)
     * 3-2. IN 사용하는 방법(select)
     *
     * 한계 : from절에는 서브쿼리 못씀.
     * 해결 : 1) 서브쿼리를 join 으로 변경
     *       2) 애플리케이션에서 쿼리를 2번 분리해서 실행
     *       3) nativeSQL 사용 (최후의 방법)
     */
    @Test
    public void subQuery() throws Exception {
        //서브쿼리는 메인쿼리랑 쿼리명이 달라야해서 하나 더 만들어줌
        QMember memberSub = new QMember("memberSub");
        //given
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                )).fetch();

        //when
        assertThat(result).extracting("age").containsExactly(40);

        //then

    }

    @Test
    public void subQuery2() throws Exception {

        QMember memberSub = new QMember("memberSub");
        //given
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                )).fetch();

        //when
        assertThat(result).extracting("age").containsExactly(30, 40);

        //then

    }

    @Test
    public void subQuery3() throws Exception {

        QMember memberSub = new QMember("memberSub");
        //given
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                )).fetch();

        //when
        assertThat(result).extracting("age").containsExactly(20, 30, 40);

        //then

    }

    @Test
    public void selectSubQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");


        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);

        }

    }

    /**
     * Case 문
     */
    @Test
    public void basicCase() throws Exception {
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살").otherwise("기타"))
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }

    }


    @Test
    public void complexCase() throws Exception {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }


    }

    /**
     * 상수(constant)
     */

    @Test
    public void constant() throws Exception {

        //given
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    /**
     * 문자 더하기
     *
     * stringValue -> 문자가 아닌 값들을 String 으로 변환해준다.
     */
    @Test
    public void concat() throws Exception {

        // {username}-----{age}
        //given
        List<String> member1 = queryFactory
                .select(member.username.concat("-----").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : member1) {
            System.out.println("s = " + s);
        }


    }

    /**
     * 프로젝션
     *  1) 기본
     *  2) 튜플
     */
    @Test
    public void simpleProjection() throws Exception {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
    @Test
    public void tupleProjection() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            System.out.println("username = " + username);
            Integer age = tuple.get(member.age);
            System.out.println("age = " + age);
        }
    }

    /**
     * 프로젝션 -DTO 반환
     *  1) JPQL - DTO 반환 (new 사용)
     *  2) Query DSL - DTO 반환
     *    - setter(프로퍼티 접근법) : DTO에 setter, 기본 생성자 필요
     *    - field 이용 : setter 필요없이 바로 필드에 적용
     *    - constructor 이용
     *
     */
    @Test
    public void findDtoByJPQL() throws Exception {
        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

        //then
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //setter 이용
    @Test
    public void findDtoBySetter() throws Exception {

        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        //then
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //Field 이용
    @Test
    public void findDtoByField() throws Exception {

        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        //then
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    //Constructor 이용
    @Test
    public void findDtoByConstructor() throws Exception {

        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        //then
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //그 외 (field 방식의 여러가지 들)
    @Test
    public void findUserDto() throws Exception {
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),  // username -> name(UserDto에는 name으로 되어있음
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub),"age")
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }


    }

    /**
     * 프로젝션 결과 반환 @QueryProjection
     * 장점 : compile 오류로 코드 오타를 잡아낼 수 있다.
     * 단점 : QueryDsl에 의존성을 가짐.
     */
    @Test
    public void findDtoByQueryProjection() throws Exception {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }






}
