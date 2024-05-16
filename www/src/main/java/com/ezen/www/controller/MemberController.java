package com.ezen.www.controller;

import com.ezen.www.domain.MemberVO;
import com.ezen.www.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/member/*")
@RequiredArgsConstructor
@Slf4j
@Controller
public class MemberController {
    private final MemberService msv;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public void join(){}

    @PostMapping("/register")
    public String register(MemberVO mvo){
        log.info(">>> mvo >> {}", mvo);
        mvo.setPwd(passwordEncoder.encode(mvo.getPwd()));
        int isOk = msv.insert(mvo);
        return "/index";
    }

    @GetMapping("/login")
    public void login(){}

//    @PostMapping("/login")
//    public String loginPost(){
//        return "/member/login?error";
//    }

    @GetMapping("/list")
    public String list(Model m){
        List<MemberVO> list = msv.getList();
        m.addAttribute("list", list);
        return "/member/list";
    }

//    private void logout(HttpServletRequest request, HttpServletResponse response){
//        Authentication authentication = SecurityContextHolder
//                .getContext()
//                .getAuthentication();
//        new SecurityContextLogoutHandler().logout(request,response,authentication);
//    }

    @GetMapping("/modify")
    public void modify(){}

    @PostMapping("/modify")
    public String modify(MemberVO mvo){
       log.info("modify {}", mvo);
       if(mvo.getPwd().isEmpty() || mvo.getEmail() == null){
           int isOk = msv.modifyNoPwd(mvo);
       }else {
           mvo.setPwd(passwordEncoder.encode(mvo.getPwd()));
           int isOk = msv.modify(mvo);
       }

        return "redirect:/member/logout";
    }
}
