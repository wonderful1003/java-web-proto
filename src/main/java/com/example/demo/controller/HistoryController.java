package com.example.demo.controller;

import com.example.demo.entity.CalculationHistory;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.repository.CalculationHistoryRepository;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 계산 히스토리 컨트롤러
 * 물타기 계산 기록 조회
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final UserService userService;
    private final MenuService menuService;
    private final CalculationHistoryRepository historyRepository;

    @GetMapping
    public String history(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        List<CalculationHistory> histories = historyRepository.findByUser_UsernameOrderByCreatedAtDesc(username);

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("histories", histories);

        return "history";
    }

    @PostMapping("/delete/{id}")
    public String deleteHistory(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        CalculationHistory history = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found"));

        // 본인 히스토리만 삭제 가능
        if (history.getUser().getUsername().equals(username)) {
            historyRepository.delete(history);
        }

        return "redirect:/history";
    }
}
