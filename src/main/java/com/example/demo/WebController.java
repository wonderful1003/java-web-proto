package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/calculator")
    public String calculator() {
        return "redirect:/index.html";
    }
}
