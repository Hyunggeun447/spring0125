package study.study.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.study.domain.entity.member.Member;
import study.study.repository.memberRepository.MemberRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void basic() throws Exception {

        //given
        Member member = new Member("abcd", "1111", "john");

        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("member" + i, "1" + i, "john" + i));
        }

        //when
//        memberRepository.findAll();
        Member result = memberRepository.findByLoginId("member5").get(0);
//
//        //then
        assertThat(result.getPassword()).isEqualTo("15");


    }

}