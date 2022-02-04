package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Controller 는 반환 값이 String이면 뷰 이름으로 인식 -> 뷰를 찾고 뷰를 랜더링함
 * @RestController 는 반환 값을 HTTP 메세지 바디에 바로 입력하여 결과값을 창에서 확인할 수 있다.
 */

@RestController
@Slf4j
public class LogTestController {
//    private final Logger log = LoggerFactory.getLogger(getClass()); //@Slf4j 로 생략가능

    @GetMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        log.trace("trace log = {}",name);
        log.debug("debug log = {}",name);
        log.info("info log = {}", name);
        log.warn("warn log = {}",name);
        log.error("error log = {}",name);

        return "ok";
    }
}
