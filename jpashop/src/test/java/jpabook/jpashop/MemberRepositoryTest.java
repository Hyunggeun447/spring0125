package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional //Test에 있으면 자동 롤백해줌
    @Rollback(value = false) // 롤백하기 싫으면 false하면 롤백안됌
    void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member result = memberRepository.find(saveId);

        //then
        assertThat(result.getId()).isEqualTo(member.getId());
        assertThat(result.getUsername()).isEqualTo(member.getUsername());

        System.out.println("result = " + result);
        System.out.println("member = " + member);
        assertThat(result).isEqualTo(member);
    }

}