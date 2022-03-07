package com.API.api.domain.dto;

import lombok.Data;

@Data
public class MemberAndTeamTypeDto {

    private Long memberId;

    private String loginName;

    public MemberAndTeamTypeDto(Long memberId, String loginName) {
        this.memberId = memberId;
        this.loginName = loginName;
    }
}
