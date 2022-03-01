package study.querydsl;


import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

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
}
