package com.API.api.domain.repository;

import com.API.api.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t from Team t" +
            " where t.teamName like %:keyword%")
    List<Team> searchTeamByKeyword(@Param("keyword") String keyword);
}
