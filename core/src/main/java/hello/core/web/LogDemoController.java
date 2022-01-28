package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
//    private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

    /**
     기본적으로 @RequiredArgsConstructor 덕분에 final이 붙은 객체는 자동으로 스프링 빈에 등록이 된다.
     그러나 실행해보면 잘 동작하지 않는다.
     왜냐면 myLogger는 MyLogger를 따르고 MyLogger를 확인해보면 scope의 value가 request로 되어있다.
     그러므로 myLogger의 생존 범위는 request가 들어와서 나갈떄까지이다.
     즉, 처음에는 요청이 없으므로 아무리 myLogger를 달라고해도 줄수가 없어서 에러가 난다.
     이것을 해결하기 위해서는 provider를 사용하면 된다.
     */

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
//        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }

}
