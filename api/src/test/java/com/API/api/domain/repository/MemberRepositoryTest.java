package com.API.api.domain.repository;

import com.API.api.domain.entity.Member;
import com.API.api.domain.entity.MemberType;
import com.API.api.domain.entity.Team;
import com.API.api.domain.entity.TeamType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

//    @BeforeEach
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

        Assertions.assertThat(result).isEqualTo(member);
        Assertions.assertThat(result.getTeam()).isEqualTo(member.getTeam());
        Assertions.assertThat(teamResult).isEqualTo(team);




    }

}