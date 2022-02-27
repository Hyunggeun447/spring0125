package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void saveAndFind() throws Exception {

        //given
        Member member = new Member("userA");

        //when
        Member result = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(result.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);

    }


    @Test
    public void JPA() throws Exception {

        //given
        Member member = new Member("userA");

        //when
        Member result = memberRepository.save(member);
        Member findMember = memberRepository.findById(result.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void CRUD() throws Exception {

        //given
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        //when

        Member findMemberA = memberJpaRepository.findById(memberA.getId()).get();
        Member findMemberB = memberJpaRepository.findById(memberB.getId()).get();

        //then
        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(memberA);
        memberJpaRepository.delete(memberB);

        long countDelete = memberJpaRepository.count();
        assertThat(countDelete).isEqualTo(0);
    }

    @Test
    public void basicCRUD() throws Exception {

        //given
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when

        Member findMemberA = memberRepository.findById(memberA.getId()).get();
        Member findMemberB = memberRepository.findById(memberB.getId()).get();

        //then
        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(memberA);
        memberRepository.delete(memberB);

        long countDelete = memberRepository.count();
        assertThat(countDelete).isEqualTo(0);

    }

    @Test
    public void findByUserNameAndOldAge() throws Exception {

        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userA", 30, null);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        //when

        List<Member> findMembers = memberJpaRepository.findByUserNameAndOldAge("userA", 25);

        //then

        assertThat(findMembers.size()).isEqualTo(1);
    }
    @Test
    public void findByUserNameAndOldAgeJPA() throws Exception {

        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userA", 30, null);

        memberRepository.save(member1);
        memberRepository.save(member2);
        //when

        List<Member> findMembers = memberRepository.findByUserNameAndAgeGreaterThan("userA", 25);

        //then
        System.out.println("================== findMember ================ ");

        for (Member findMember : findMembers) {
            System.out.println("findMember = " + findMember);
        }

        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    public void namedQuery() throws Exception {

        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userA", 30, null);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        //when

        List<Member> result = memberJpaRepository.findByUserName("userA");


        //then
        Member member = result.get(0);
        assertThat(member).isEqualTo(member1);

    }
    @Test
    public void namedQueryJpa() throws Exception {

        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userA", 30, null);

        memberRepository.save(member1);
        memberRepository.save(member2);
        //when

        List<Member> result = memberRepository.findByUserName("userA");

        //then
        System.out.println("================== findMember ================ ");
        for (Member member : result) {
            System.out.println("member = " + member);
        }

        Member member = result.get(0);
        assertThat(member).isEqualTo(member1);

    }

    @Test
    public void testQuery() throws Exception {
        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userA", 30, null);

        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<Member> userAs = memberRepository.findUser("userA", 20);

        //then
        for (Member member : userAs) {
            System.out.println("member = " + member);
        }
        assertThat(userAs.get(0)).isEqualTo(member1);

    }

    @Test
    public void findUserNameList() throws Exception {

        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userB", 30, null);

        memberRepository.save(member1);
        memberRepository.save(member2);


        List<String> userNameList = memberRepository.findUserNameList();
        System.out.println("================================================== ");

        for (String s : userNameList) {
            System.out.println("userName = " + s);
        }

        //then

    }

    @Test
    public void findMemberDto() throws Exception {

        //given
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);
        Member m1 = new Member("AAA", 20, teamA);
        memberRepository.save(m1);

        //when

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        System.out.println("================================================== ");
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
        //then

    }

    @Test
    public void findByNames() throws Exception {

        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userB", 30, null);

        memberRepository.save(member1);
        memberRepository.save(member2);


        List<Member> byNames = memberRepository.findByNames(Arrays.asList("userA", "userB"));
        System.out.println("================================================== ");
        for (Member byName : byNames) {
            System.out.println("result = " + byName);
        }

        //then

    }

    @Test
    public void returnTypeTest() throws Exception {

        //given
        Member member1 = new Member("userA", 20, null);
        Member member2 = new Member("userB", 30, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> resultList = memberRepository.findListByUserName("userA");
        Member result = memberRepository.findMemberByUserName("userA");
        Optional<Member> resultOptional = memberRepository.findOptionalByUserName("userA");

        System.out.println("================================================== ");

        System.out.println("resultList = " + resultList);
        System.out.println("result = " + result);
        System.out.println("resultOptional = " + resultOptional.get());


        //then

    }

    @Test
    public void paging() throws Exception {

        //given
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 10, null));
        memberJpaRepository.save(new Member("member3", 10, null));
        memberJpaRepository.save(new Member("member4", 10, null));
        memberJpaRepository.save(new Member("member5", 10, null));

        int age = 10;
        int offset = 0;
        int limit = 3;



        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //then
        assertThat(members.size()).isEqualTo(limit);
        assertThat(totalCount).isEqualTo(5);

    }


    /**
     *
     * DATA JPA 페이징, 정렬
     * 반환타입 (Page, Slice, List)
     *
     * 1) Page = 결과 + count쿼
     * Page 는 토탈카운트때문에 느림.
     * -> @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
     * 카운트 쿼리를 분리시켜서 성능을 향상시킬 수 있다.
     *
     * 2) 슬라이스 쓸때 = > 모바일에서 좋다.
     * 슬라이스는 요청의 +1개를 가져와서 잘라서 처리해줌.
     * 그렇기에 count 쿼리 없이 다음 페이지 유무를 확인 가능하다.
     * 대신 토탈 카운팅 기능은 없다.
     *
     * 3)List는 추가 count 쿼리 없이 결과만 얻고싶을때.
     *
     */
    @Test
    public void dataJpaPaging() throws Exception {

        //given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));


        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

//        Dto 로 변환하고 싶으면 map을 쓰면된다.
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUserName(), null));

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();


        System.out.println("================================================== ");

        for (Member member : content) {
            System.out.println("member = " + member);
        }
//        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);                //1개 페이지의 크기
        assertThat(page.getTotalElements()).isEqualTo(5);       //총 요소의 개수
        assertThat(page.getNumber()).isEqualTo(0);              //현재 페이지 넘버
        assertThat(page.getTotalPages()).isEqualTo(2);          //총 페이지의 개수
        assertThat(page.isFirst()).isTrue();                    //페이지가 첫번째인가?
        assertThat(page.hasNext()).isTrue();                    //다음 페이지가 있는가?
    }

    /**
     * 순수 JPA bulk update
     */
    @Test
    public void bulkUpdate() throws Exception {

        //given
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 20, null));
        memberJpaRepository.save(new Member("member3", 21, null));
        memberJpaRepository.save(new Member("member4", 30, null));
        memberJpaRepository.save(new Member("member5", 31, null));

        //when

        int resultCount = memberJpaRepository.bulkAge(20);

        //then

        assertThat(resultCount).isEqualTo(3);

    }

    /**
     * Data Jpa bulk update
     */
    @Test
    public void dataJpaBulkUpdate() throws Exception {

        //given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 20, null));
        memberRepository.save(new Member("member3", 21, null));
        memberRepository.save(new Member("member4", 30, null));
        memberRepository.save(new Member("member5", 31, null));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);


//        em.flush();
//        em.clear();

        List<Member> member5s = memberRepository.findByUserName("member5");
        Member member5 = member5s.get(0);
        System.out.println("member5 = " + member5);

        //then

        assertThat(resultCount).isEqualTo(3);


    }

    /**
     * Entity Graph
     */

    @Test
    public void findMemberLazy() throws Exception {

        //given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 20, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        em.flush();
        em.clear();

        List<Member> all = memberRepository.findAll();
//        List<Member> all = memberRepository.findMemberFetchJoin();

        for (Member member : all) {
            System.out.println("member = " + member.getUserName());
            System.out.println("member = " + member.getTeam().getClass());
            System.out.println("member = " + member.getTeam().getName());
        }

        //then

    }

    /**
     * Hint
     */
    @Test
    public void queryHint() throws Exception {

        //given
        Member member1 = new Member("member1", 20, null);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
//        Member findMember = memberRepository.findById(member1.getId()).get();
        Member findMember = memberRepository.findReadOnlyByUserName("member1");
        findMember.setUserName("member2");

        em.flush();
        //then

    }

    /**
     * Lock
     */

    @Test
    public void lock() throws Exception {

        //given
        Member member1 = new Member("member1", 20, null);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when

        List<Member> findMember = memberRepository.findLockByUserName("member1");
        //자동으로 for update 가 붙는다.

        //then

    }

    /**
     * 확장기능
     * 사용자 정의 repository 구현
     * Query DSL 때 유용하게 사용할 수 있음.
     */

    @Test
    public void callCustom() throws Exception {

        //given
        List<Member> memberCustom = memberRepository.findMemberCustom();

        for (Member member : memberCustom) {
            System.out.println("member = " + member);
        }

        //when

        //then

    }

    /**
     * Auditing
     * MappedSuperClass를 이용한 공통객체(생성일, 수정일) 생성, 적용
     */
    @Test
    public void jpaEventBaseEntity() throws Exception {

        //given

        Member member1 = new Member("member1", 20, null);
        memberRepository.save(member1); // @PrePersist

        Thread.sleep(100);

        member1.setUserName("member2");
        em.flush(); //@PreUpdate
        em.clear();

        //when

        Member member = memberRepository.findById(member1.getId()).get();

        //then
        System.out.println("member create date = " + member.getCreateDate());
//        System.out.println("member update date = " + member.getUpdateDate());
        System.out.println("member update date = " + member.getLastModifiedDate());
        System.out.println("member create by = " + member.getCreatedBy());
        System.out.println("member update by = " + member.getLastModifiedBy());


    }



}