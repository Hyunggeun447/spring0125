package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("timeProxy 실행");
        long startTime = System.currentTimeMillis();

//        Object result = method.invoke(target, objects);
        Object result = invocation.proceed(); // 여기서 타겟을 찾아서 호출해줌.

        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("timeProxy 종료, resultTime = {}", resultTime);
        return result;
    }
}
