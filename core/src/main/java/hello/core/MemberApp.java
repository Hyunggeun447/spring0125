package hello.core;

import hello.core.membar.Grade;
import hello.core.membar.Member;
import hello.core.membar.MemberService;
import hello.core.membar.MemberServiceImpl;

import java.sql.SQLOutput;

public class MemberApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new Member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());

    }
}
