package study.querydsl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

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


}
