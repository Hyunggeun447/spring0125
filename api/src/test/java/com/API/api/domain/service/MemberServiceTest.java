package com.API.api.domain.service;

import com.API.api.domain.dto.MemberAndTeamTypeDto;
import com.API.api.domain.entity.Member;
import com.API.api.domain.entity.MemberType;
import com.API.api.domain.entity.Team;
import com.API.api.domain.entity.TeamType;
import com.API.api.domain.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamService teamService;

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

    @Test
    public void findByTeamName() throws Exception {

        //given
        Member member = new Member("loginName", "memberName", "password1!", "e@mail.com");

        Long memberId = memberService.save(member);
        Team team = new Team("teamName", TeamType.OTHERS);
        teamRepository.save(team);

        member.addTeam(team);

        //when

        List<Member> resultList = memberService.findByTeamName("teamName");

        for (Member member1 : resultList) {
            System.out.println("member1 = " + member1.getMemberName());
        }
        //then

        assertThat(resultList.size()).isEqualTo(1);
        assertThat(resultList).contains(member);


    }

    @Test
    public void editTest() throws Exception {

        //given
        Member member = new Member("loginName", "memberName", "password1!", "e@mail.com");

        Long memberId = memberService.save(member);
        Team team = new Team("teamName", TeamType.OTHERS);
        teamRepository.save(team);

        member.addTeam(team);

        //when
        Team sameTeamName = new Team("teamName", TeamType.DEVELOPER);

        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> teamService.save(sameTeamName));



    }

    @Test
    public void typeSearch() throws Exception {

        //given

        //given
        Member member = new Member("loginName", "memberName", "password1!", "e@mail.com");

        Long memberId = memberService.save(member);
        member.updateType(MemberType.JUNIOR);
        Team team = new Team("teamName", TeamType.OTHERS);
        teamRepository.save(team);

        member.addTeam(team);

        //when
        List<MemberAndTeamTypeDto> resultList = memberService.findByMemberTypeAndTeamType(MemberType.JUNIOR, TeamType.OTHERS);
        for (MemberAndTeamTypeDto memberAndTeamTypeDto : resultList) {
            System.out.println("memberAndTeamTypeDto = " + memberAndTeamTypeDto.getMemberId());
            System.out.println("memberAndTeamTypeDto = " + memberAndTeamTypeDto.getLoginName());
        }
        //then
        assertThat(resultList.size()).isEqualTo(1);

    }

    @Test
    public void paging() throws Exception {

        //given

        Member member1 = new Member("loginName1", "memberName", "password1!", "e@mail.com");
        Member member2 = new Member("loginName2", "memberName", "password1!", "e@mail.com");
        Member member3 = new Member("loginName3", "memberName", "password1!", "e@mail.com");
        Member member4 = new Member("loginName4", "memberName", "password1!", "e@mail.com");

        Long memberId1 = memberService.save(member1);
        Long memberId2 = memberService.save(member2);
        Long memberId3 = memberService.save(member3);
        Long memberId4 = memberService.save(member4);
        member1.updateType(MemberType.JUNIOR);
        member2.updateType(MemberType.JUNIOR);
        member3.updateType(MemberType.SENIOR);
        member4.updateType(MemberType.SENIOR);
        Team team1 = new Team("teamName1", TeamType.OTHERS);
        Team team2 = new Team("teamName2", TeamType.DEVELOPER);
        teamRepository.save(team1);
        teamRepository.save(team2);

        member1.addTeam(team1);
        member2.addTeam(team1);
        member3.addTeam(team2);
        member4.addTeam(team2);

        //when
        PageRequest pag = PageRequest.of(0, 5);
        Page<Member> result = memberService.findByTeamPlusPaging("teamName1", pag);

        for (Member member : result) {
            System.out.println("member = " + member.getLoginName());
        }

        //then
        Pageable pageable = result.getPageable();
        System.out.println("pageable = " + pageable);


    }

}