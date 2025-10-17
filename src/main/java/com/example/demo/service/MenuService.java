package com.example.demo.service;

import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 메뉴 관리 서비스
 * 사용자 권한에 따른 메뉴 조회 로직 처리
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findByVisibleTrueOrderBySortOrder();
    }

    public List<Menu> getMenusForUser(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return menuRepository.findByRoleNamesAndVisible(roleNames);
    }
}
