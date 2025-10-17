package com.example.demo.config;

import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있으면 스킵
        if (roleRepository.count() > 0) {
            return;
        }

        // 1. 역할(Role) 생성
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole.setDescription("관리자");
        adminRole = roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        userRole.setDescription("일반 사용자");
        userRole = roleRepository.save(userRole);

        // 2. 메뉴 생성
        Menu menu1 = createMenu("대시보드", "/dashboard", "🏠", null, 1);
        Menu menu2 = createMenu("물타기 계산", "/calculator", "📊", null, 2);
        Menu menu3 = createMenu("포트폴리오", "/portfolio", "💼", null, 3);
        Menu menu4 = createMenu("계산 히스토리", "/history", "📜", null, 4);
        Menu menu5 = createMenu("내 정보", "/profile", "👤", null, 5);
        Menu menu6 = createMenu("회원 관리", "/admin/users", "👥", null, 6);
        Menu menu7 = createMenu("권한 관리", "/admin/roles", "🔑", null, 7);
        Menu menu8 = createMenu("메뉴 관리", "/admin/menus", "📋", null, 8);
        Menu menu9 = createMenu("시스템 설정", "/admin/settings", "⚙️", null, 9);

        menu1 = menuRepository.save(menu1);
        menu2 = menuRepository.save(menu2);
        menu3 = menuRepository.save(menu3);
        menu4 = menuRepository.save(menu4);
        menu5 = menuRepository.save(menu5);
        menu6 = menuRepository.save(menu6);
        menu7 = menuRepository.save(menu7);
        menu8 = menuRepository.save(menu8);
        menu9 = menuRepository.save(menu9);

        // 3. 메뉴-역할 매핑
        Set<Menu> adminMenus = new HashSet<>();
        adminMenus.add(menu1);
        adminMenus.add(menu2);
        adminMenus.add(menu3);
        adminMenus.add(menu4);
        adminMenus.add(menu6);
        adminMenus.add(menu7);
        adminMenus.add(menu8);
        adminMenus.add(menu9);
        adminRole.setMenus(adminMenus);
        roleRepository.save(adminRole);

        Set<Menu> userMenus = new HashSet<>();
        userMenus.add(menu1);
        userMenus.add(menu2);
        userMenus.add(menu3);
        userMenus.add(menu4);
        userMenus.add(menu5);
        userRole.setMenus(userMenus);
        roleRepository.save(userRole);

        // 4. 사용자 생성
        // 관리자 계정: admin / admin123
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setName("관리자");
        admin.setEmail("admin@example.com");
        admin.setEnabled(true);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        admin.setRoles(adminRoles);
        userRepository.save(admin);

        // 일반 사용자 계정: user / user123
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setName("일반 사용자");
        user.setEmail("user@example.com");
        user.setEnabled(true);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);

        System.out.println("=================================");
        System.out.println("초기 데이터 로딩 완료!");
        System.out.println("관리자: admin / admin123");
        System.out.println("사용자: user / user123");
        System.out.println("=================================");
    }

    private Menu createMenu(String name, String path, String icon, Long parentId, int sortOrder) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPath(path);
        menu.setIcon(icon);
        menu.setParentId(parentId);
        menu.setSortOrder(sortOrder);
        menu.setVisible(true);
        return menu;
    }
}
