package com.store.chichi.repository;

import com.store.chichi.domain.Address;
import com.store.chichi.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

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
    public List<Member> findByAddress(String nickname) {
        return em.createQuery("select m from Member m where m.nickname= :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }
}
