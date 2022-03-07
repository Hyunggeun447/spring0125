package com.API.api.domain.apiController;

import com.API.api.domain.dto.MemberDto;
import com.API.api.domain.entity.Member;
import com.API.api.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberControllerApi {

    private final MemberService memberService;


    /**
     * Body- text
     */
    @PostMapping("/v1")
    public MemberDto findByLoginName(@RequestBody String loginName) {
        Member member = memberService.findByLoginName(loginName);

        MemberDto memberDto = new MemberDto(member.getLoginName(), member.getMemberName());
        return memberDto;
    }

    /**
     * 쿼리 파라미터 방식
     */
    @PostMapping("/v2")
    public MemberDto findByLoginName2(@RequestParam String loginName) {
        Member member = memberService.findByLoginName(loginName);
        MemberDto memberDto = new MemberDto(member.getLoginName(), member.getMemberName());
        return memberDto;
    }

    /**
     * 쿼리 파라미터 방식
     */
    @PostMapping("/v3")
    public MemberDto findByLoginName3(@ModelAttribute FindForm form) {
        String loginName = form.getLoginName();
        Member member = memberService.findByLoginName(loginName);
        MemberDto memberDto = new MemberDto(member.getLoginName(), member.getMemberName());
        return memberDto;
    }

    /**
     * Body -JSON -> key:value
     *
     */
    @PostMapping("/v4")
    public MemberDto findByLoginName4(@RequestBody FindForm form) {
        String loginName = form.getLoginName();
        Member member = memberService.findByLoginName(loginName);
        MemberDto memberDto = new MemberDto(member.getLoginName(), member.getMemberName());
        return memberDto;

    }
}
