package com.store.chichi.service;

import com.store.chichi.domain.Member;
import com.store.chichi.repository.memberRepository.MemberRepository;
import com.store.chichi.service.loginService.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LoginService loginService;

    @Test
    public void loginServcie() throws Exception {

        //given
        Member kim = new Member("kim", "111", "123", "123", "4213");
        memberService.join(kim);
        Member login = loginService.login("111", "1123");


        System.out.println("========================================");
        System.out.println(login);


        //when

        //then

    }

}