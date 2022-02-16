package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  //읽기만 하는 곳에는 readOnly=true 하면 성능 최적화 가능. 읽기가 많아서 전체적으로 리드온니를 주고 회원가입부분에만 따로 false를 줬다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*//    @Autowired
//    @Autowired //생성자 하나면 자동으로 Autowired 해줌
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    세터 인젝션 잘 안씀
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    //회원 가입
    @Transactional // 이곳은 쓰기이므로 readOnly = false. (default)이므로 생략
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증.
        memberRepository.save(member);
        return member.getId();
    }

    //중복 회원 검증 메서드
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    //회원 단일 id 조회
    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
