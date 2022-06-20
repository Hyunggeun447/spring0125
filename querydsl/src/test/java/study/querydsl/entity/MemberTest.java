package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;


@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void 엔티티테스트() throws Exception {

        //given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 20, teamB);
        Member member4 = new Member("member4", 20, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        //when

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        //then

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("team = " + member.getTeam());
        }

    }



}