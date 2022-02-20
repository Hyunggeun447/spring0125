package com.store.chichi.repository;

import com.store.chichi.domain.Address;
import com.store.chichi.domain.Member;
import com.store.chichi.domain.MemberGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;




    public void save(Member member) {
        em.persist(member);
    }

    //아이디로 조회
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    //전체목록 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 이름으로 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    // 주소로 조회
    public List<Member> findByAddress(Address address) {
        return em.createQuery("select m from Member m where m.address= :address", Member.class)
                .setParameter("address", address)
                .getResultList();
    }

    // 닉네임으로 조회
    public List<Member> findByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname= :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    /**
     * 로그인 시스템
     * findAll로 모든 member list를 얻는다.
     * stream으로 list을 훑음.
     * filter로 nickName이 같은 member를 구함.
     * @return
     */
    public Optional<Member> findByLoginId(String nickName) {
        return findAll().stream().filter(member -> member.getNickname().equals(nickName)).findFirst();
    }


}
