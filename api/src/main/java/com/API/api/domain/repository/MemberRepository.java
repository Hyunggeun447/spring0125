package com.API.api.domain.repository;

import com.API.api.domain.dto.MemberAndTeamTypeDto;
import com.API.api.domain.entity.Member;
import com.API.api.domain.entity.MemberType;
import com.API.api.domain.entity.TeamType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByLoginName(String loginName);

    List<Member> findByEmail(String email);

    @Query("select m from Member m join m.team t where t.teamName = :teamName")
    List<Member> findByTeamName(@Param("teamName") String teamName);


    @Query("select m from Member m join m.team t where m.memberType = :memberType and t.teamType = :teamType")
    List<Member> findByMemberTypeAndTeamType(@Param("memberType") MemberType memberType,
                                             @Param("teamType") TeamType teamType);

    @Query("select new com.API.api.domain.dto.MemberAndTeamTypeDto(m.id, m.loginName) from Member m join m.team t" +
            " where m.memberType = :memberType and t.teamType = :teamType")
    List<MemberAndTeamTypeDto> findByMemberTypeAndTeamTypeDto(@Param("memberType") MemberType memberType,
                                                              @Param("teamType") TeamType teamType);


    @Query(value = "select m from Member m join m.team t where t.teamName = :teamName",
            countQuery = "select count(m) from Member m")
    Page<Member> findByTeamNameAndPaging(@Param("teamName") String teamName, Pageable pageable);

    @Query(value = "select new com.API.api.domain.dto.MemberAndTeamTypeDto(m.id, m.loginName) " +
            "from Member m join m.team t where t.teamName = :teamName", countQuery = "select count (m) from Member m")
    Page<MemberAndTeamTypeDto> findByTeamNameAndPagingDto(@Param("teamName") String teamName, Pageable pageable);




}
