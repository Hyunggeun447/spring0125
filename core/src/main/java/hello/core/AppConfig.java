package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // 멤버 저장소는 MemoryMemberRep 로
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

//    // 할인 정책은 Fix 로
//    @Bean
//    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
//    }

    // 할인 정책을 Rate로 변경
    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }

    //멤버 서비스는 멤버 임플로, 멤버 임플은 멤버 저장소로
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    // 오더 서비스는 오더 서비스 임블을, 오더 서비스 임블은 맴버 저장소, 할인 정책을 사용.
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

}
