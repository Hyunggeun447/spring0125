package com.store.chichi.service;


import com.store.chichi.domain.Member;
import com.store.chichi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;


    /**
     *
     * @param nickName
     * @param password
     * @return memberRepository의 nickName이 같은 member를 구해서 password가 같은 memeber가 있으면 반환. 없으면 null
     */
    public Member login(String nickName, String password) {
        return memberRepository.findByLoginId(nickName).filter(m -> m.getPassword().equals(password)).orElse(null);
    }
}
