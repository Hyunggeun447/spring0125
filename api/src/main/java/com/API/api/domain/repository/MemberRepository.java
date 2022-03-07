package com.API.api.domain.repository;

import com.API.api.domain.entity.Member;
import com.API.api.domain.entity.MemberType;
import com.API.api.domain.entity.TeamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByLoginName(String loginName);

    List<Member> findByEmail(String email);

    @Query("select m from Member m join m.team t where m.memberType = :memberType and t.teamType = :teamType")
    List<Member> findByMemberTypeAndMemberType(@Param("memberType") MemberType memberType,
                                               @Param("teamType") TeamType teamType);

}
