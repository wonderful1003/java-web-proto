package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 대시보드 컨트롤러
 * 로그인 후 메인 대시보드 페이지 및 사용자별 메뉴 제공
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final MenuService menuService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        // 사용자 메뉴 가져오기
        List<Menu> menus = menuService.getMenusForUser(user);

        // 관리자 여부 확인
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);

        return "dashboard";
    }
}
