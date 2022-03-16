package hello.proxy.pureproxy.concreteProxy;

import hello.proxy.pureproxy.concreteProxy.code.ConcreteClient;
import hello.proxy.pureproxy.concreteProxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteProxy.code.TimeProxy;
import hello.proxy.pureproxy.decorator.code.TimeDecorator;
import org.junit.jupiter.api.Test;

public class ConcreteProxyTest {

    @Test
    public void noProxy() throws Exception {

        //given
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();

    }

    @Test
    public void addProxy() throws Exception {

        //given
        ConcreteLogic concreteLogic = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient client = new ConcreteClient(timeProxy);
        client.execute();

    }
}
