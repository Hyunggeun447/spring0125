package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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
     * 1) List 반환에 값이 없을경우 => Null이 아니라 empty collection으로 반환된다.
     * 2) Member 반환은 Null이 반환
     * - 원래 예외(NoResultException(가 터지나(순수 jpa의 경우), Data Jpa의 경우 자체적으로 null로 반환해준다.
     * 3) 그냥 Optional 쓰면 null이든 뭐든 해결된다.
     * 다만 Optional 결과가 2개이상이 나올경우 예외가 발생. (Optional은 단건조회니까)
     */

    List<Member> findListByUserName(String usrName); // 컬렉션 반환

    Member findMemberByUserName(String usrName); // 단건 반환

    Optional<Member> findOptionalByUserName(String usrName); //단건 Optional 반환


    /**
     * Data JPA 사용한 페이징, 정렬, 반환타입
     * <p>
     * 자세한 내용은 테스트(dataJpaPaging) 참조.
     */

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m", countQuery = "select count(m) from Member m")
    Page<Member> findByAgeV2(int age, Pageable pageable);


    /**
     * DATA JPA bulk update
     * <p>
     * 주의점 : bulk update를 할 경우 db의 데이터를 업데이트한다.
     * 하지만 영속성 컨텍스트의 값은 변하지 있는다.
     * 즉, 이 상태에서 조회한다면? -> 업데이트 미적용된 데이터가 결과로 나옴.
     * 그래서 벌크 업데이트 이후에는 영속성 컨텍스를 처리해줘야 함
     * 1) @Modifying(clearAutomatically = true)
     * 2) 수동으로 엔티티매니져 (flush, clear)
     */

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age>:age")
    int bulkAgePlus(@Param("age") int age);


    /**
     * fecth join 적용
     *
     * @EntityGraph(attributePaths = {"team"})
     * 를 사용하면 패치조인을 할 수 있다.
     */

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUserName(@Param("userName") String userName);

    /**
     * Hint
     */

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(String userName);

    /**
     * Lock
     */

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUserName(String userName);

    /**
     * Projections
     * 원하는 데이터만 부분 요청
     */
//    List<UserNameOnly> findProjectionsByUserName(@Param("userName") String userName);
    List<UserNameOnlyDto> findProjectionsByUserName(@Param("userName") String userName);




}
