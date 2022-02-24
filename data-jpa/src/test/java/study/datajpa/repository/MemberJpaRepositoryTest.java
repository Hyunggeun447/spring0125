package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void saveAndFind() throws Exception {

        //given
        Member member = new Member("userA");

        //when
        Member result = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(result.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);

    }


    @Test
    public void JPA() throws Exception {

        //given
        Member member = new Member("userA");

        //when
        Member result = memberRepository.save(member);
        Member findMember = memberRepository.findById(result.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);

    }

}