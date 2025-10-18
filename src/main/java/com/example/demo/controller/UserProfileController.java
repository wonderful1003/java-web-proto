package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 사용자 프로필 관리 컨트롤러
 * 내정보 조회, 수정, 회원탈퇴 기능 제공
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserProfileController {

    private final UserService userService;
    private final MenuService menuService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 내정보 조회 페이지
     */
    @GetMapping
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);

        return "profile";
    }

    /**
     * 내정보 수정 페이지
     */
    @GetMapping("/edit")
    public String editForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);

        return "profile-edit";
    }

    /**
     * 내정보 수정 처리
     */
    @PostMapping("/edit")
    public String edit(@RequestParam String name,
                      @RequestParam String email,
                      @RequestParam(required = false) String currentPassword,
                      @RequestParam(required = false) String newPassword,
                      @RequestParam(required = false) String confirmPassword,
                      Authentication authentication,
                      RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        // 기본 정보 업데이트
        user.setName(name);
        user.setEmail(email);

        // 비밀번호 변경 요청이 있는 경우
        if (newPassword != null && !newPassword.isEmpty()) {
            // 현재 비밀번호 확인
            if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
                return "redirect:/profile/edit";
            }

            // 새 비밀번호 확인
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "새 비밀번호가 일치하지 않습니다.");
                return "redirect:/profile/edit";
            }

            // 비밀번호 변경
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "정보가 성공적으로 수정되었습니다.");

        return "redirect:/profile";
    }

    /**
     * 회원탈퇴 확인 페이지
     */
    @GetMapping("/delete")
    public String deleteConfirm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);

        return "profile-delete";
    }

    /**
     * 회원탈퇴 처리
     */
    @PostMapping("/delete")
    public String delete(@RequestParam String password,
                        Authentication authentication,
                        RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/profile/delete";
        }

        // 관리자는 탈퇴 불가
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (isAdmin) {
            redirectAttributes.addFlashAttribute("error", "관리자 계정은 탈퇴할 수 없습니다.");
            return "redirect:/profile/delete";
        }

        // 계정 비활성화 (실제 삭제 대신)
        user.setEnabled(false);
        userRepository.save(user);

        // 로그아웃 처리를 위해 로그인 페이지로 리다이렉트
        return "redirect:/logout";
    }
}
