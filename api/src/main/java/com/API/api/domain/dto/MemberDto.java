package com.API.api.domain.dto;

import com.API.api.domain.entity.Team;
import lombok.Data;

@Data
public class MemberDto {

    private String loginName;

    private String memberName;

    public MemberDto(String loginName, String memberName) {
        this.loginName = loginName;
        this.memberName = memberName;
    }
}
