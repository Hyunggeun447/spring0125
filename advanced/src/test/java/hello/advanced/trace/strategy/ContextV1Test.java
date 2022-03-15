package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    public void strategyV0() throws Exception {
        logic1();
        logic2();

    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비지니스 로직 실행
        log.info("비지니스 로직1 실행");
        //비지니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비지니스 로직 실행
        log.info("비지니스 로직2 실행");
        //비지니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }

    /**
     * 템플릿 메서드 패턴 적용
     */
    @Test
    public void templateMethodV1() throws Exception {

        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();

    }

    @Test
    public void templateMethodV2() throws Exception {
        AbstractTemplate template1 = new AbstractTemplate() {

            @Override
            protected void call() {
                log.info("비지니스 로직 1 실행");

            }
        };

        log.info("클래스 이름 = {}", template1.getClass());

        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {

            @Override
            protected void call() {
                log.info("비지니스 로직 2 실행");

            }
        };

        log.info("클래스 이름 = {}", template2.getClass());


        template2.execute();

    }

    @Test
    public void strategyV1() throws Exception {

        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();
        StrategyLogic2 strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();

    }

    @Test
    public void strategyV2() throws Exception {

        Strategy strategyLogic1 = new Strategy() {

            @Override
            public void call() {
                log.info("비지니스 로직 1 실행");

            }
        };
        ContextV1 context1 = new ContextV1(strategyLogic1);
        log.info("strategyLogic1={}", strategyLogic1.getClass());

        context1.execute();

        Strategy strategyLogic2 = new Strategy() {

            @Override
            public void call() {
                log.info("비지니스 로직 2 실행");

            }
        };
        ContextV1 context2 = new ContextV1(strategyLogic2);
        log.info("strategyLogic2={}", strategyLogic2.getClass());
        context2.execute();


    }

    @Test
    public void strategyV3() throws Exception {

        ContextV1 context1 = new ContextV1(new Strategy() {

            @Override
            public void call() {
                log.info("비지니스 로직 1 실행");

            }
        });

        context1.execute();

        ContextV1 context2 = new ContextV1(new Strategy() {

            @Override
            public void call() {
                log.info("비지니스 로직 2 실행");

            }
        });

        context2.execute();

    }

    @Test
    public void strategyV4() throws Exception {

        ContextV1 context1 = new ContextV1(() -> log.info("비지니스 로직 1 실행"));

        context1.execute();

        ContextV1 context2 = new ContextV1(() -> log.info("비지니스 로직 2 실행"));

        context2.execute();


    }
}
