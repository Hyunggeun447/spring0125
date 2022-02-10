package hello.itemservice.web.validation;

import hello.itemservice.web.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {
    /**
     * RequestBody로 body 데이터를 사용할 때
     * Json에서 데이터 형식 오류가 발생하면 바로 예외가 발생하고 컨트롤러까지도 오지 않는다.
     * 즉 log.info(api 컨트롤러 호출) 자체가 뜨질 않는다.
     * 이전 ModelAttribute는 예외 발생 안하고 validated할 수 있었던것과는 상반됌.
     */

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("오류 발생. error = {}",bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");

        return form;

    }

}
