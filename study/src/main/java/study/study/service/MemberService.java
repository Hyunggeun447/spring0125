package study.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.study.domain.entity.member.Member;
import study.study.repository.memberRepository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(Member member) {
        validateLoginName(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateLoginName(Member member) {
        String loginId = member.getLoginId();
        List<Member> byLoginId = memberRepository.findByLoginId(loginId);
        if (!byLoginId.isEmpty()) {
            throw new IllegalStateException("존재하는 아아디");
        }
    }

    public void updatePassword(Long id, String password, String newPassword) {
        Member member = memberRepository.findById(id).get();
        if (member.getPassword() != password) {
            throw new IllegalStateException("비밀번호가 다릅니다");
        }
        member.updatePassword(newPassword);
    }





}
