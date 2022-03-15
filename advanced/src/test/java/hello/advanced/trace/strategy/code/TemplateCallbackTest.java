package hello.advanced.trace.strategy.code;

import hello.advanced.trace.strategy.code.template.Callback;
import hello.advanced.trace.strategy.code.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackTest {

    /**
     *  템플릿 콜백 패턴 - 익명 내부 클래스
     */
    @Test
    public void callbackV1() throws Exception {

        //given

        TimeLogTemplate timeLogTemplate = new TimeLogTemplate();
        timeLogTemplate.execute(new Callback() {
            @Override
            public void call() {
                log.info("비지니스 로직 1 실행");
            }
        });

        timeLogTemplate.execute(new Callback() {
            @Override
            public void call() {
                log.info("비지니스 로직 2 실행");
            }
        });

    }

    /**
     *  템플릿 콜백 패턴 - 람다
     */
    @Test
    public void callbackV2() throws Exception {

        //given

        TimeLogTemplate timeLogTemplate = new TimeLogTemplate();

        timeLogTemplate.execute(() -> log.info("비지니스 로직 1 실행"));

        timeLogTemplate.execute(() -> log.info("비지니스 로직 2 실행"));

    }
}
