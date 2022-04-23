package com.API.api.domain.apiController;

import com.API.api.domain.dto.MemberAndTeamTypeDto;
import com.API.api.domain.dto.MemberDto;
import com.API.api.domain.entity.Member;
import com.API.api.domain.repository.MemberRepository;
import com.API.api.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MemberControllerApi {

    private final MemberService memberService;

    private final MemberRepository memberRepository;


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
     */
    @PostMapping("/v4")
    public MemberDto findByLoginName4(@RequestBody FindForm form) {
        String loginName = form.getLoginName();
        Member member = memberService.findByLoginName(loginName);
        MemberDto memberDto = new MemberDto(member.getLoginName(), member.getMemberName());
        return memberDto;
    }

    @PostMapping("/v5/{teamName}")
    public Page<MemberAndTeamTypeDto> findByTeamPagingDto(@PathVariable(value = "teamName", required = false) String teamName) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "loginName"));
        return memberService.findByTeamPlusPagingDto(teamName, pageRequest);
    }

    @PostMapping("/v6")
    public Page<MemberDto> findByTypesPagingDto2(@PageableDefault(size = 5, sort = "id") Pageable pageable) {
        Page<Member> all = memberRepository.findAll(pageable);
        Page<MemberDto> map = all.map(member -> new MemberDto(member.getLoginName(), member.getMemberName()));
        return map;
    }
}
