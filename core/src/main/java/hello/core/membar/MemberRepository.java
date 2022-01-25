package hello.core.membar;

public interface MemberRepository {

    void save(Member member);

    Member findByID(Long memberID);

}
