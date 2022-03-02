package study.study.repository.memberRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.study.domain.entity.member.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustum {


    List<Member> findByName(String name);

    List<Member> findByLoginId(String loginId);

}
