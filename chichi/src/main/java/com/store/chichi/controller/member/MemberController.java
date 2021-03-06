package com.store.chichi.controller.member;


import com.store.chichi.domain.Address;
import com.store.chichi.domain.Member;
import com.store.chichi.domain.MemberGrade;
import com.store.chichi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberCreateForm", new MemberCreateForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Validated @ModelAttribute MemberCreateForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }


        if (!form.getPassword().equals(form.getPassword2())) {
//            throw new IllegalStateException();
            bindingResult.reject("different password","비밀번호 오류");

            return "members/createMemberForm";
        }

        Address address = new Address(form.getAddress1(),form.getAddress2());
        Member member = new Member(
            form.getName(), form.getLoginName(), form.getPassword(),
            form.getEMail(), form.getPhoneNumber(), address);
        memberService.join(member);
        return "redirect:/";

    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> allMembers = memberService.findAllMembers();
        model.addAttribute("members", allMembers);
        return "members/memberList";
    }
}
