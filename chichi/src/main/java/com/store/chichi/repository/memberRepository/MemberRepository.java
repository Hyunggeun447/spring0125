package com.store.chichi.repository.memberRepository;

import com.store.chichi.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Member findMemberByLoginName(String loginName);
    List<Member> findByLoginName(String loginName);

    List<Member> findByName(String name);

    List<Member> findByLoginNameAndPassword(String loginName, String password);



}
