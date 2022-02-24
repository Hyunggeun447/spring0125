package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepositoryJpaData extends JpaRepository<Member,Long> {

    //자동으로 select m from Member m where m.name = ?
    List<Member> findByName(String name);
}
