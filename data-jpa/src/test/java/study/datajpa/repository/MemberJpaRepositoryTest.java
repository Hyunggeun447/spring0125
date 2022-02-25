package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    MemberRepository memberRepository;

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


}