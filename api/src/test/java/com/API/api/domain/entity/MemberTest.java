package com.API.api.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

//    @BeforeEach

    @Test
    public void 엔티티테스트() throws Exception {

        //given
        Member member = new Member("loginName", "name", "123!", "email@xxx.com");
        Team team = new Team("teamName", TeamType.DEVELOPER);

        //when
        member.updateType(MemberType.JUNIOR);
        member.addTeam(team);
        member.updatePassword("!123");
        team.updateType(TeamType.OTHERS);

        //then

        assertThat(member.getLoginName()).isEqualTo("loginName");
        assertThat(member.getPassword()).isEqualTo("!123");
        assertThat(member.getEmail()).isEqualTo("email@xxx.com");
        assertThat(member.getMemberType()).isEqualTo(MemberType.JUNIOR);
        assertThat(member.getTeam().getTeamName()).isEqualTo("teamName");
        assertThat(member.getTeam().getTeamType()).isEqualTo(TeamType.OTHERS);

    }

}