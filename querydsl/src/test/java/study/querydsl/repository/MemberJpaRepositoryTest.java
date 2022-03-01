package study.querydsl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

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


}