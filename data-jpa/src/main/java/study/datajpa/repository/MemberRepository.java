package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserNameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    /**
     * NameQuery 는 활용도가 떨어져서 잘 사용안한다.
     *
     * 장점 : 오타를 컴파일 오류로 잡아낼 수 있음.
     * 이유 : 정적쿼리라 파싱할때 잡아낼 수 있음.
     */
//    @Query(name = "Member.findByUserName") // 없어도 됌. nameQuery가 메소드 쿼리보다 우선순위가 높기때문.
    List<Member> findByUserName(@Param("userName") String userName);
}
