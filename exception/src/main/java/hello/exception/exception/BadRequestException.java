package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason =  "error.bad")
//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason =  "찾을 수 없읍니다.")
public class BadRequestException extends RuntimeException {

}
