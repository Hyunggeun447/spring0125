package com.API.api.domain.repository;

import com.API.api.domain.dto.MemberAndTeamTypeDto;
import com.API.api.domain.entity.Member;
import com.API.api.domain.entity.MemberType;
import com.API.api.domain.entity.Team;
import com.API.api.domain.entity.TeamType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @BeforeEach
    public void beforeEach() {
        for (int i = 0; i < 10; i++) {
            Member member = new Member("loginName" + i, "memberName" + i, "!password" + i, "e" + i + "@xxx.com");
            Team team;
            if (i % 2 == 0) {
                team = new Team("teamName" + i, TeamType.DEVELOPER);
                member.updateType(MemberType.JUNIOR);
            } else {
                team = new Team("teamName" + i, TeamType.OTHERS);
                member.updateType(MemberType.SENIOR);
            }
            member.addTeam(team);
            memberRepository.save(member);
            teamRepository.save(team);
        }
    }

    @Test
    public void 저장() throws Exception {

        //given
        Member member = new Member("loginName", "memberName", "!password", "e@xxx.com");
        Team team = new Team("teamName", TeamType.DEVELOPER);

        member.addTeam(team);
        memberRepository.save(member);
        teamRepository.save(team);
        //when
        Member result = memberRepository.findById(member.getId()).get();

        Team teamResult = teamRepository.findById(member.getTeam().getId()).get();

        //then

        assertThat(result).isEqualTo(member);
        assertThat(result.getTeam()).isEqualTo(member.getTeam());
        assertThat(teamResult).isEqualTo(team);

    }

    @Test
    public void findByLoginName() throws Exception {

        //when
        Member result = memberRepository.findMemberByLoginName("loginName0");

        //then
        assertThat(result.getPassword()).isEqualTo("!password0");

    }

    @Test
    public void findByEmail() throws Exception {

        //when
        List<Member> resultList = memberRepository.findByEmail("e1@xxx.com");

        //then
        assertThat(resultList).containsExactly(memberRepository.findMemberByLoginName("loginName1"));
    }

    @Test
    public void findByMemberTypeAndTeamType() throws Exception {

        //given

        //when
        List<Member> resultList = memberRepository.findByMemberTypeAndTeamType(MemberType.JUNIOR, TeamType.DEVELOPER);

        //then
        assertThat(resultList.size()).isEqualTo(5);
        assertThat(resultList).contains(memberRepository.findMemberByLoginName("loginName8"));

    }

    @Test
    public void findByMemberTypeAndTeamTypeDTO() throws Exception {

        //when

        List<MemberAndTeamTypeDto> result = memberRepository.findByMemberTypeAndTeamTypeDto(MemberType.JUNIOR, TeamType.DEVELOPER);

        //then
        for (MemberAndTeamTypeDto memberAndTeamTypeDto : result) {
            System.out.println("memberAndTeamTypeDto = " + memberAndTeamTypeDto);
        }

    }

    @Test
    public void searchTeam() throws Exception {

        //when
        List<Team> resultList = teamRepository.searchTeamByKeyword("am");
        for (Team team : resultList) {
            System.out.println("team = " + team.getTeamName());
        }

        //then
        assertThat(resultList.size()).isEqualTo(10);
//        assertThat(resultList).isEqualTo(teamRepository.findAll());

    }

}