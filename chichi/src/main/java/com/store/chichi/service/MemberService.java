package com.store.chichi.service;

import com.store.chichi.domain.Member;
import com.store.chichi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        checkSameNameMember(member);
        memberRepository.save(member);
        return member.getId();

    }

    private void checkSameNameMember(Member member) {

        List<Member> byName = memberRepository.findByName(member.getName());
        if (!byName.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> findByName(String memberName) {
        return memberRepository.findByName(memberName);
    }
}
