package com.API.api.domain.service;

import com.API.api.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Test
    public void saveAndValidate() throws Exception {

        //given
        Member member = new Member("loginName", "memberName", "password1!", "e@mail.com");
        memberService.save(member);
        //when
        Member sameLoginNameMember = new Member("loginName", "memberName1", "password2@", "e1@mail.com");

        //then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> memberService.save(sameLoginNameMember));

    }

    @Test
    public void NotBlank_Pattern_언제_발생() throws Exception {

        //given
        Member illegalMember = new Member("1", "memberName", "aa", "a");

        //when
        memberService.save(illegalMember);

        //then
        //assertThrows할때 발생하는듯?

    }

    @Test
    public void findById_LoginName() throws Exception {

        //given
        Member member = new Member("loginName", "memberName", "password1!", "e@mail.com");
        Long memberId = memberService.save(member);

        //when
        Member result1 = memberService.findById(memberId);
        Member result2 = memberService.findByLoginName("loginName");

        //then
        assertThat(result1).isEqualTo(result2).isEqualTo(member);
    }

}