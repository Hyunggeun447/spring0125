package com.API.api.domain.repository;

import com.API.api.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByLoginName(String loginName);

    List<Member> findByEmail(String email);

}
