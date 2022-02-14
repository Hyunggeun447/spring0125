package hello.typeconverter.controller;

import hello.typeconverter.IpPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {


    //문자로 request를 받고 문자 data로 만든 뒤 타입을 숫자로 바꿔서 intValue에 저장
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); //문자타입 조회
        Integer intValue = Integer.valueOf(data);
        System.out.println("intValue = " + intValue);
        return "ok";
    }

    //그냥 숫자타입으로 data를 받아서 반환
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort = " + ipPort.getPort());
        System.out.println("ipPort = " + ipPort.getIp());
        System.out.println("ipPort = " + ipPort);
        return "ok";
    }
}
