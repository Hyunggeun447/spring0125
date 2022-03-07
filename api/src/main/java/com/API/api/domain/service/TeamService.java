package com.API.api.domain.service;

import com.API.api.domain.entity.Team;
import com.API.api.domain.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Long save(Team team) {
        validateSameTeamName(team);
        teamRepository.save(team);
        return team.getId();
    }

    private void validateSameTeamName(Team team) {
        if (teamRepository.findTeamByTeamName(team.getTeamName()) != null) {
            throw new IllegalStateException("이미 존재하는 팀명");
        }
    }
}
