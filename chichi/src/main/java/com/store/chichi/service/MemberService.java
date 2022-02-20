package com.store.chichi.service;

import com.store.chichi.domain.Address;
import com.store.chichi.domain.Member;
import com.store.chichi.domain.MemberGrade;
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
        checkSameNicknameMember(member);
        memberRepository.save(member);
        return member.getId();

    }

    private void checkSameNicknameMember(Member member) {

        List<Member> byNickname = memberRepository.findByNickname(member.getNickname());
        if (!byNickname.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    @Transactional
    public void updatePassword(Long memberId, String password) {
        Member member = memberRepository.findById(memberId);
        member.setPassword(password);
    }

    @Transactional
    public void updateMember(Long memberId, String email, String phoneNumber, Address address) {
        Member member = memberRepository.findById(memberId);
        member.setEMail(email);
        member.setAddress(address);
        member.setPhoneNumber(phoneNumber);
    }

    @Transactional
    public void updateMemberGrade(Long memberId, MemberGrade grade) {
        Member member = memberRepository.findById(memberId);
        member.setMemberGrade(grade);
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
