package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUserName();
    }

    /**
     * 바로 Member을 넣어도 위의 과정을 알아서 잘 처리해줌 => 도메인 클래스 컨버터
     *
     * 비추 : 실제로 사용하기엔 예제처럼 단순한 경우가 많이 없으며, 코드 해석하기 힘들다.
     * 주의점 : 조회용으로만 사용해야한다.
     *      1) 트랜잭션이 없는 범위에서 엔티티를 조회했으므로
     *      2) 영속성 컨텍스트가 관리하는 경우가 아니며
     *      3) 그러므로 수정을 했을때 더티체킹이 발생하시 않는다.
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUserName();
    }

    /**
     * 페이징과 정렬 기능
     * 설정법
     * 1) 쿼리파라미터로 직접 입력해서 page, size , sort 설정할 수 있음.
     *      ex)?page=0&size=10&sort=id,desc&sort=userName,desc
     * 2) application.yml 에 글로벌 설정
     * 3) @PageableDefault 설정
     *
     */
    @GetMapping("members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "userName") Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> result = page.map(member -> new MemberDto(member));
        return result;
    }

    @PostConstruct
    public void init() {

        for (int i = 0; i <100; i++) {
            memberRepository.save(new Member("user" + i, i, null));

        }

    }
}
