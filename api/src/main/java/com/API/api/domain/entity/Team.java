package com.API.api.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TeamType teamType;

    public Team(String teamName, TeamType teamType) {
        this.teamName = teamName;
        this.teamType = teamType;
    }

    public void updateType(TeamType type) {
        this.teamType = type;
    }


}
