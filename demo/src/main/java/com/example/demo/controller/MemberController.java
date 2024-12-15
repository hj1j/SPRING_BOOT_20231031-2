package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;
import com.example.demo.model.domain.Member;
import org.springframework.ui.Model;

    @Controller
    public class MemberController {
    @Autowired
    private MemberService memberService; //의존성주입
    
    @GetMapping("/join_new") // 회원 가입 페이지 연결
    public String join_new() {
        return "join_new"; // .HTML 연결
    }
    

    @PostMapping("/api/members") // 회원 가입 저장
    public String addmembers(@ModelAttribute AddMemberRequest request) {
        memberService.saveMember(request);
        return "join_end"; // .HTML 연결
    }

    @GetMapping("/member_login") // 로그인페이지연결
    public String member_login() {
        return "login"; // .HTML 연결
    }
    
    @PostMapping("/api/login_check") // 로그인(아이디, 패스워드)체크
    public String checkMembers(@ModelAttribute AddMemberRequest request, Model model) {
    try {
        Member member= memberService.loginCheck(request.getEmail(), request.getPassword()); // 패스워드반환
        model.addAttribute("member", member); // 로그인성공시회원정보전달
        return "redirect:/board_list"; // 로그인성공후이동할페이지
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage()); // 에러메시지전달
        return "login"; // 로그인실패시로그인페이지로리다이렉트
    }
}
}
