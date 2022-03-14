package hello.advanced.trace.helloTrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloTraceV2Test {

    @Test
    public void begin_end() throws Exception {

        //given
        HelloTraceV2 traceV2 = new HelloTraceV2();
        TraceStatus status1 = traceV2.begin("hello");
        TraceStatus status2 = traceV2.beginSync(status1.getTraceId(), "hello2");

        traceV2.end(status2);
        traceV2.end(status1);

    }

    @Test
    public void begin_exception() throws Exception {

        //given
        HelloTraceV2 traceV2 = new HelloTraceV2();
        TraceStatus status1 = traceV2.begin("hello");
        TraceStatus status2 = traceV2.beginSync(status1.getTraceId(), "hello");
        traceV2.exception(status2, new IllegalStateException());
        traceV2.exception(status1, new IllegalStateException());

    }

}