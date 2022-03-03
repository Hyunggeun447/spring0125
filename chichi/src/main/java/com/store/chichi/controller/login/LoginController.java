package com.store.chichi.controller.login;


import com.store.chichi.domain.Member;
import com.store.chichi.service.loginService.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("form", new LoginForm());
        return "login/loginForm";
    }

    /**
     *
     * @param form
     * @param bindingResult
     * @param request HttpServletRequest에서 getSession. ==> session 에 loginMember 저장
     * @param redirectURL 나중에 login 요구할때 login 성공 후 원래 URL로 보내주기 위한 재료
     * @return
     */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("form") LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURL) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginName(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디, 비밀번호 오류");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();

        session.setAttribute("loginMember", loginMember);

//        log.info("login comp");
        return "redirect:" + redirectURL;

    }

    /**
     *
     * @param request HttpServletRequest에서 session을 얻는다. false ==> 세션이 없을경우 null 반환.
     *                session 이 null이 아닐경우 유효기간을 없앤다. invalidate()
     * @return
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
//        log.info("logout comp");
        return "redirect:/";
    }
}
