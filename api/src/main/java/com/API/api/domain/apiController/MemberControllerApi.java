package com.API.api.domain.apiController;

import com.API.api.domain.dto.MemberDto;
import com.API.api.domain.entity.Member;
import com.API.api.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberControllerApi {

    private final MemberService memberService;

    @PostMapping("/v1")
    public MemberDto findByLoginName(@RequestBody String loginName) {
        Member member = memberService.findByLoginName(loginName);

        MemberDto memberDto = new MemberDto(member.getLoginName(), member.getMemberName());
        return memberDto;
    }


}
