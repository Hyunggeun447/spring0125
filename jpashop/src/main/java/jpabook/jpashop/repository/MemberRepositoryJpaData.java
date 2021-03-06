package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepositoryJpaData extends JpaRepository<Member,Long> {

    //μλμΌλ‘ select m from Member m where m.name = ?
    List<Member> findByName(String name);
}
