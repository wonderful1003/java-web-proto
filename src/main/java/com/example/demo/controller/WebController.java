package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인 페이지 라우팅 컨트롤러
 * 루트 경로와 계산기 페이지 리다이렉트 처리
 */
@Controller
public class WebController {

    /**
     * 루트 경로 접근 시 로그인 페이지로 리다이렉트
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    /**
     * 계산기 페이지 접근 (인증 필요)
     */
    @GetMapping("/calculator")
    public String calculator() {
        return "redirect:/index.html";
    }
}
