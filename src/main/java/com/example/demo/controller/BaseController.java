package com.example.demo.controller;

import com.example.demo.constant.RoleType;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;

import java.util.List;

/**
 * 모든 컨트롤러의 공통 기능을 제공하는 베이스 컨트롤러
 *
 * 반복적으로 사용되는 로직을 추출하여 코드 중복을 제거합니다:
 * - 사용자 정보 조회
 * - 메뉴 정보 조회
 * - 관리자 권한 확인
 * - 모델에 공통 속성 추가
 *
 * 모든 컨트롤러는 이 클래스를 상속받아 공통 기능을 재사용할 수 있습니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseController {

    /**
     * 사용자 서비스 - 사용자 정보 조회용
     */
    protected final UserService userService;

    /**
     * 메뉴 서비스 - 역할 기반 메뉴 조회용
     */
    protected final MenuService menuService;

    /**
     * 인증 정보에서 현재 로그인한 사용자를 조회합니다.
     *
     * @param authentication Spring Security 인증 객체
     * @return 조회된 사용자 엔티티
     * @throws com.example.demo.exception.ResourceNotFoundException 사용자를 찾을 수 없을 때
     */
    protected User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        log.debug("현재 사용자 조회: {}", username);
        return userService.getUserByUsername(username);
    }

    /**
     * 사용자의 역할에 따라 표시할 메뉴 목록을 조회합니다.
     *
     * @param user 사용자 엔티티
     * @return 사용자가 접근 가능한 메뉴 목록
     */
    protected List<Menu> getMenusForUser(User user) {
        log.debug("사용자 메뉴 조회: {}", user.getUsername());
        return menuService.getMenusForUser(user);
    }

    /**
     * 인증 정보에서 관리자 권한 여부를 확인합니다.
     *
     * @param authentication Spring Security 인증 객체
     * @return 관리자이면 true, 아니면 false
     */
    protected boolean isAdmin(Authentication authentication) {
        boolean hasAdminRole = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleType.ADMIN.getAuthority()));
        log.debug("관리자 권한 확인: {}", hasAdminRole);
        return hasAdminRole;
    }

    /**
     * 모델에 공통 속성을 추가합니다.
     *
     * 모든 페이지에서 공통적으로 필요한 속성들을 한 번에 추가합니다:
     * - user: 현재 로그인한 사용자 정보
     * - menus: 사용자가 접근 가능한 메뉴 목록
     * - isAdmin: 관리자 권한 여부
     *
     * @param model          Spring MVC 모델 객체
     * @param authentication Spring Security 인증 객체
     */
    protected void addCommonAttributes(Model model, Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<Menu> menus = getMenusForUser(user);
        boolean isAdmin = isAdmin(authentication);

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);

        log.debug("공통 속성 추가 완료 - 사용자: {}, 메뉴 수: {}, 관리자: {}",
                user.getUsername(), menus.size(), isAdmin);
    }

    /**
     * 리소스 소유자 권한을 확인합니다.
     *
     * 리소스의 소유자이거나 관리자인 경우에만 true를 반환합니다.
     * 포트폴리오, 게시글 등의 수정/삭제 권한 검증에 사용됩니다.
     *
     * @param resourceOwnerUsername 리소스 소유자의 사용자명
     * @param currentUsername       현재 로그인한 사용자명
     * @param isAdmin               현재 사용자가 관리자인지 여부
     * @return 권한이 있으면 true, 없으면 false
     */
    protected boolean hasPermission(String resourceOwnerUsername,
                                    String currentUsername,
                                    boolean isAdmin) {
        boolean hasPermission = resourceOwnerUsername.equals(currentUsername) || isAdmin;
        log.debug("권한 확인 - 리소스 소유자: {}, 현재 사용자: {}, 관리자: {}, 결과: {}",
                resourceOwnerUsername, currentUsername, isAdmin, hasPermission);
        return hasPermission;
    }

    /**
     * 권한이 없을 때 예외를 발생시킵니다.
     *
     * @param message 에러 메시지
     * @throws com.example.demo.exception.AccessDeniedException 항상 발생
     */
    protected void throwAccessDenied(String message) {
        log.warn("접근 거부: {}", message);
        throw new com.example.demo.exception.AccessDeniedException(message);
    }
}
