package hello.proxy.pureproxy.concreteProxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteClient {

    private ConcreteLogic concreteLogic;

    public ConcreteClient(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute() {
        log.info("ConcreteClient 실행");
        concreteLogic.operation();
    }
}
