package com.example.demo.controller;

import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 대시보드 컨트롤러
 *
 * 로그인 후 표시되는 메인 대시보드 페이지를 처리합니다.
 * 사용자 정보와 역할 기반 메뉴를 표시합니다.
 *
 * BaseController를 상속받아 공통 로직을 재사용합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@Controller
public class DashboardController extends BaseController {

    /**
     * 생성자 주입
     *
     * @param userService 사용자 서비스
     * @param menuService 메뉴 서비스
     */
    public DashboardController(UserService userService, MenuService menuService) {
        super(userService, menuService);
    }

    /**
     * 대시보드 페이지
     *
     * 사용자 정보와 접근 가능한 메뉴를 모델에 추가하여 대시보드를 표시합니다.
     *
     * @param authentication Spring Security 인증 객체
     * @param model          뷰에 전달할 모델
     * @return 대시보드 뷰 이름
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        log.info("대시보드 접근 - 사용자: {}", authentication.getName());

        // BaseController의 공통 메서드를 사용하여 모델에 속성 추가
        addCommonAttributes(model, authentication);

        return "dashboard";
    }
}
