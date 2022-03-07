package com.API.api;

import com.API.api.domain.entity.Member;
import com.API.api.domain.entity.MemberType;
import com.API.api.domain.entity.Team;
import com.API.api.domain.entity.TeamType;
import com.API.api.domain.repository.MemberRepository;
import com.API.api.domain.repository.TeamRepository;
import com.API.api.domain.service.MemberService;
import com.API.api.domain.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


@Component
@RequiredArgsConstructor
public class Init {

    private final InitService initService;

    @PostConstruct
    public void initDb() {
        initService.init1();
        initService.init2();
        initService.init3();
        initService.init4();
    }


    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {

        private final EntityManager em;


        public void init1() {

            for (int i = 0; i < 25; i++) {
                Member member = new Member("loginNameA" + i, "memberNameA" + i, "password!A" + i, "e@mail.com");
                em.persist(member);

                member.updateType(MemberType.JUNIOR);
                Team team = new Team("teamNameA" + i, TeamType.OTHERS);
                em.persist(team);
                member.addTeam(team);
            }

        }

        public void init2() {

            for (int i = 25; i < 50; i++) {
                Member member = new Member("loginNameA" + i, "memberNameA" + i, "password!A" + i, "e@mail.com");
                em.persist(member);

                member.updateType(MemberType.SENIOR);
                Team team = new Team("teamNameA" + i, TeamType.DEVELOPER);
                em.persist(team);
                member.addTeam(team);
            }

        }

        public void init3() {

            for (int i =50; i < 75; i++) {
                Member member = new Member("loginNameA" + i, "memberNameA" + i, "password!A" + i, "e@mail.com");
                em.persist(member);

                member.updateType(MemberType.SENIOR);
                Team team = new Team("teamNameA" + i, TeamType.OTHERS);
                em.persist(team);
                member.addTeam(team);
            }

        }

        public void init4() {

            for (int i = 75; i < 100; i++) {
                Member member = new Member("loginName" + i, "memberName" + i, "password!" + i, "e@mail.com");
                em.persist(member);

                member.updateType(MemberType.JUNIOR);
                Team team = new Team("teamName" + i, TeamType.DEVELOPER);
                em.persist(team);
                member.addTeam(team);
            }

        }

    }
}
