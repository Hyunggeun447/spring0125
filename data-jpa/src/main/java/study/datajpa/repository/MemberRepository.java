package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserNameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    /**
     * NameQuery 는 활용도가 떨어져서 잘 사용안한다.
     * <p>
     * 장점 : 오타를 컴파일 오류로 잡아낼 수 있음.
     * 이유 : 정적쿼리라 파싱할때 잡아낼 수 있음.
     */
//    @Query(name = "Member.findByUserName") // 없어도 됌. nameQuery가 메소드 쿼리보다 우선순위가 높기때문.
    List<Member> findByUserName(@Param("userName") String userName);


    /**
     * Query, Repository 메소드에 쿼리 정의
     * 장점 : 1) 메소드명 간략화, 2) 이것 역시 어플리케이션 로딩때 오류로 잡아낼 수 있다.
     */
    @Query("select m from Member m where m.userName = :userName and m.age = :age")
    List<Member> findUser(@Param("userName") String userName, @Param("age") int age);

    /**
     * 특정값을 조회
     */
    @Query("select m.userName from Member m")
    List<String> findUserNameList();

    /**
     * Dto 조회
     */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();


    /**
     * in 사용해서 찾기
     */

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    /**
     * 여러가지 반환타입
     * <주의>
     *    1) List 반환에 값이 없을경우 => Null이 아니라 empty collection으로 반환된다.
     *    2) Member 반환은 Null이 반환
     *      - 원래 예외(NoResultException(가 터지나(순수 jpa의 경우), Data Jpa의 경우 자체적으로 null로 반환해준다.
     *    3) 그냥 Optional 쓰면 null이든 뭐든 해결된다.
     *       다만 Optional 결과가 2개이상이 나올경우 예외가 발생. (Optional은 단건조회니까)
     */

    List<Member> findListByUserName(String usrName); // 컬렉션 반환

    Member findMemberByUserName(String usrName); // 단건 반환

    Optional<Member> findOptionalByUserName(String usrName); // 단건 Optional 반환

}
