package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class PrototypeTest {

    //프로토타입은 싱글톤 유지 x 스프링컨테이너가 생성, 의존관계 주입, 초기화까지만 관여. 그래서 종료 메서드는 실행x
    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);

        System.out.println("prototypeBean1 = " + bean1);
        System.out.println("prototypeBean2 = " + bean2);
        Assertions.assertThat(bean1).isNotSameAs(bean2);

//        bean1.destroy(); // 종료는 클라이언트가 직접 해야한다.
//        bean2.destroy();

        ac.close();


    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }

}
