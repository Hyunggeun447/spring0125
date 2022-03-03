package com.store.chichi.service.loginService;


import com.store.chichi.domain.Member;
import com.store.chichi.repository.memberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;


    /**
     *
     * @param loginName
     * @param password
     * @return memberRepository의 loginName 같은 member를 구해서 password가 같은 memeber가 있으면 반환. 없으면 null
     */
    public Member login(String loginName, String password) {
        List<Member> findLoginNameAndPassword = memberRepository.findByLoginNameAndPassword(loginName, password);
        if (!findLoginNameAndPassword.isEmpty()) {
            return findLoginNameAndPassword.get(0);
        } else {
            return null;
        }
    }
}
