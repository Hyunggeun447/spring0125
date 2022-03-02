package study.querydsl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void basic() throws Exception {

        Member member = new Member("member", 20, null);
        memberJpaRepository.save(member);

        Member findByIdMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findByIdMember).isEqualTo(member);

        List<Member> findAllMember = memberJpaRepository.findAll();
        assertThat(findAllMember).containsExactly(member);

        List<Member> findByUsernameMember = memberJpaRepository.findByUsername("member");
        assertThat(findByUsernameMember).containsExactly(member);

        // ============================ DSL 버전 ================================
        List<Member> findAllMemberDsl = memberJpaRepository.findAllDsl();
        assertThat(findAllMemberDsl).containsExactly(member);

        List<Member> findByUsernameMemberDsl = memberJpaRepository.findByUsernameDsl("member");
        assertThat(findByUsernameMemberDsl).containsExactly(member);


    }

    /**
     * 동적쿼리 builder 테스트
     */
    @Test
    public void searchTest() throws Exception {

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

        //when

        MemberSearchCondition condition = new MemberSearchCondition();
//        condition.setAgeGoe(35);
//        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
        //then
        assertThat(result).extracting("username").containsExactly("member3", "member4");

    }

    /**
     * 동적쿼리 where 테스트
     */
    @Test
    public void searchTestWhere() throws Exception {

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

        //when

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.search(condition);
        //then
        assertThat(result).extracting("username").containsExactly("member4");

    }


}