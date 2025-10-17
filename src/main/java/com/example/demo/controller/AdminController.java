package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 기능 컨트롤러
 * 회원, 권한, 메뉴 관리
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MenuService menuService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    /**
     * 회원 관리 페이지
     */
    @GetMapping("/users")
    public String users(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        List<User> users = userService.getAllUsers();
        List<Menu> menus = menuService.getMenusForUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", true);
        model.addAttribute("users", users);

        return "admin-users";
    }

    /**
     * 회원 활성화/비활성화 토글
     */
    @PostMapping("/users/{id}/toggle-enabled")
    public String toggleUserEnabled(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(!user.getEnabled());
        userRepository.save(user);

        return "redirect:/admin/users";
    }

    /**
     * 회원 삭제
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    /**
     * 권한 관리 페이지
     */
    @GetMapping("/roles")
    public String roles(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        List<Role> roles = roleRepository.findAll();
        List<Menu> menus = menuService.getMenusForUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", true);
        model.addAttribute("roles", roles);

        return "admin-roles";
    }

    /**
     * 권한 추가
     */
    @PostMapping("/roles/add")
    public String addRole(@RequestParam String name, @RequestParam String description) {
        Role role = new Role();
        role.setName(name.startsWith("ROLE_") ? name : "ROLE_" + name);
        role.setDescription(description);
        roleRepository.save(role);

        return "redirect:/admin/roles";
    }

    /**
     * 권한 삭제
     */
    @PostMapping("/roles/{id}/delete")
    public String deleteRole(@PathVariable Long id) {
        roleRepository.deleteById(id);
        return "redirect:/admin/roles";
    }

    /**
     * 메뉴 관리 페이지
     */
    @GetMapping("/menus")
    public String menus(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        List<Menu> allMenus = menuRepository.findAll();
        List<Menu> userMenus = menuService.getMenusForUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("menus", userMenus);
        model.addAttribute("isAdmin", true);
        model.addAttribute("allMenus", allMenus);

        return "admin-menus";
    }

    /**
     * 메뉴 추가
     */
    @PostMapping("/menus/add")
    public String addMenu(@RequestParam String name,
                         @RequestParam String path,
                         @RequestParam String icon,
                         @RequestParam Integer sortOrder) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPath(path);
        menu.setIcon(icon);
        menu.setSortOrder(sortOrder);
        menu.setVisible(true);
        menuRepository.save(menu);

        return "redirect:/admin/menus";
    }

    /**
     * 메뉴 표시/숨김 토글
     */
    @PostMapping("/menus/{id}/toggle-visible")
    public String toggleMenuVisible(@PathVariable Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        menu.setVisible(!menu.getVisible());
        menuRepository.save(menu);

        return "redirect:/admin/menus";
    }

    /**
     * 메뉴 삭제
     */
    @PostMapping("/menus/{id}/delete")
    public String deleteMenu(@PathVariable Long id) {
        menuRepository.deleteById(id);
        return "redirect:/admin/menus";
    }
}
