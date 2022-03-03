package com.store.chichi.service;

import com.store.chichi.domain.Member;
import com.store.chichi.repository.memberRepository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    public void 중복회원() throws Exception {

        //given

        Member member1 = new Member("1","중복아이디","123","123","123");
        Member member2 = new Member("1","중복아이디","123","123","123");


        //when

        memberService.join(member1);

        //then
        Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member2));

    }

}