package hello.core.sington;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonTest {

    @Test
    @DisplayName("스프링없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();  // Annotation 을 거쳐서 들어온게 아니라 바로 들어온 순수한 DI 컨테이너

        // 1. 조회 : 호출할 때마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();
        // 2. 조회 : 호출할 때마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        System.out.println("memberService1 = " +memberService1);
        System.out.println("memberService2 = " +memberService2);

//        memberService1 != memberService2
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);

    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonService() {
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        System.out.println("singletonService 1 = " + singletonService1);
        System.out.println("singletonService 2 = " + singletonService2);

        Assertions.assertThat(singletonService1).isSameAs(singletonService2);
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {

        // AppConfig appConfig = new AppConfig();  // Annotation 을 거쳐서 들어온게 아니라 바로 들어온 순수한 DI 컨테이너
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class); // 윗줄과는 달리 거쳐서 들어온 스프링 컨테이너
        // 1. 조회 : 호출할 때마다 객체를 조회
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        // 2. 조회 : 호출할 때마다 객체를 조회
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        // 참조값이 같다.
        System.out.println("memberService1 = " +memberService1);
        System.out.println("memberService2 = " +memberService2);

//        memberService1 == memberService2
        Assertions.assertThat(memberService1).isSameAs(memberService2);

    }


}
