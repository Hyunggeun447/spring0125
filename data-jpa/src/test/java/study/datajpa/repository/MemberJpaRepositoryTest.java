package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
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
     * DATA JPA ?????????, ??????
     * ???????????? (Page, Slice, List)
     *
     * 1) Page = ?????? + count???
     * Page ??? ???????????????????????? ??????.
     * -> @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
     * ????????? ????????? ??????????????? ????????? ???????????? ??? ??????.
     *
     * 2) ???????????? ?????? = > ??????????????? ??????.
     * ??????????????? ????????? +1?????? ???????????? ????????? ????????????.
     * ???????????? count ?????? ?????? ?????? ????????? ????????? ?????? ????????????.
     * ?????? ?????? ????????? ????????? ??????.
     *
     * 3)List??? ?????? count ?????? ?????? ????????? ???????????????.
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

        Page<Member> page = memberRepository.findByAgeV2(age, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

//        Dto ??? ???????????? ????????? map??? ????????????.
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUserName(), null));

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();


        System.out.println("================================================== ");

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);


        int totalPages = page.getTotalPages();
        System.out.println("totalPages = " + totalPages);
        int number = page.getNumber();
        System.out.println("number = " + number);
        int numberOfElements = page.getNumberOfElements();
        System.out.println("numberOfElements = " + numberOfElements);
        int size = page.getSize();
        System.out.println("size = " + size);
        Sort sort = page.getSort();
        System.out.println("sort = " + sort);
        Class<? extends Page> aClass = page.getClass();
        System.out.println("aClass = " + aClass);
        Pageable pageable = page.getPageable();
        System.out.println("pageable = " + pageable);


        assertThat(content.size()).isEqualTo(3);                //1??? ???????????? ??????
        assertThat(page.getTotalElements()).isEqualTo(5);       //??? ????????? ??????
        assertThat(page.getNumber()).isEqualTo(0);              //?????? ????????? ??????
        assertThat(page.getTotalPages()).isEqualTo(2);          //??? ???????????? ??????
        assertThat(page.isFirst()).isTrue();                    //???????????? ????????????????
        assertThat(page.hasNext()).isTrue();                    //?????? ???????????? ??????????
    }

    /**
     * ?????? JPA bulk update
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
        //???????????? for update ??? ?????????.

        //then

    }

    /**
     * ????????????
     * ????????? ?????? repository ??????
     * Query DSL ??? ???????????? ????????? ??? ??????.
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
     * MappedSuperClass??? ????????? ????????????(?????????, ?????????) ??????, ??????
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

    /**
     * Query By Example
     *
     * ?????? : join??? ?????? ????????? ??? ??????.(inner??? ??????)
     * ???, ?????????
     */
    @Test
    public void queryByExample() throws Exception {

        //given

        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 20, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();
        
        //when

        Member member = new Member("member1");
        Team team = new Team("teamA");
        member.setTeam(team);


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);


        //then

        List<Member> result = memberRepository.findAll(example);

        assertThat(result.get(0).getUserName()).isEqualTo("member1");

    }

    /**
     * Projections
     */
    @Test
    public void findProjectionsUserNameOnly() {

        //given

        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 20, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when

//        List<UserNameOnly> result = memberRepository.findProjectionsByUserName("member1");
        List<UserNameOnlyDto> result = memberRepository.findProjectionsByUserName("member1");


        //then
        for (UserNameOnlyDto userNameOnly : result) {
            System.out.println("userNameOnly = " + userNameOnly.getUserName());
        }

    }




}