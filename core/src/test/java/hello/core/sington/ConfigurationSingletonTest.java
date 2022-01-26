package hello.core.sington;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();


        //3개가 다 같다. -> 싱글톤 유지
        System.out.println("memberService -> memberRepository = " + memberRepository1); //memberService에서 뽑아온 memberRepository
        System.out.println("orderService -> memberRepository = " + memberRepository2);//orderService에서 뽑아온 memberRepository
        System.out.println(" memberRepository = " + memberRepository); //순수한 memberRepository

        assertThat(memberRepository1).isSameAs(memberRepository2);
        assertThat(memberRepository1).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean.getClass()); //bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$9db1a227
        //임의의 다른 클래스(@Configuration을 통해 만들어진)가 싱글톤이 보장되도록 함.
        //싱글톤 원리. 생성된 클래스가 @Bean을 호출할때 if 이미 컨테이너에 있으면 반환. else if 없으면 등록 후 호출
        //만약 @Configuration이 없다면? @Bean을 통해 스프링 컨테이너는 등록이 된다. 그러나 싱글톤이 보장이 안됌(AppConfig 그 자체로 등록되서 사용되므로)

    }
}
