package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /*
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 상태코드를 200->400으로 변경.
    @ExceptionHandler(IllegalArgumentException.class) //에러가 떠도 리졸버가 해결을 해서 정상흐름이 되고 상태코드는 200이 됌.
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    *//**
     * UserException, IllegalArgumentException 각각의 자식클래스까지 예외 처리를 해줌.
     * Exception 이 예외중 상위이므로 UserException, IllegalArgumentException 이 못잡아내면 여기로 옴
     *
     *//*
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 상태코드를 200->500으로 변경.
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exception] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
    */


    @GetMapping("/api2/members/{id}")

    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자입니다.");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류입니다.");
        }

        return new MemberDto(id, "hello" + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }


}
