package hello.core.sington;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // 사용자 A, 10000원 주문
        statefulService1.order("userA", 10000);
        // 사용자 B, 20000원 주문
        statefulService2.order("userB", 20000);

        // 사용자 A의 주문 금액 조회
        int price = statefulService1.getPrice();
        // 10000원이 나와야하는데 20000원이 나온다. 값이 공유되기 때문에 이런 문제가 발생한다.
        System.out.println("price = " + price);
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig{

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}