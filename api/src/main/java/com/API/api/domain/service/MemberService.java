package com.API.api.domain.service;

import com.API.api.domain.entity.Member;
import com.API.api.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(Member member) {
        validSameLoginName(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validSameLoginName(Member member) {
        Member memberByLoginName = memberRepository.findMemberByLoginName(member.getLoginName());
        if (memberByLoginName != null) {
            throw new IllegalStateException("존재하는 아이디");
        }
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }

    public Member findByLoginName(String loginName) {
        return memberRepository.findMemberByLoginName(loginName);
    }

    public List<Member> findByTeamName(String teamName) {
        return memberRepository.findByTeamName(teamName);
    }






}
