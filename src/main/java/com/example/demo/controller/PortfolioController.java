package com.example.demo.controller;

import com.example.demo.dto.PortfolioRequest;
import com.example.demo.dto.PortfolioView;
import com.example.demo.entity.Portfolio;
import com.example.demo.entity.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.PortfolioService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 포트폴리오 컨트롤러
 *
 * 사용자의 주식 포트폴리오 관리 기능을 제공합니다:
 * - 포트폴리오 목록 조회
 * - 포트폴리오 추가
 * - 포트폴리오 삭제
 * - 투자 금액 및 손익 계산
 *
 * BaseController를 상속받아 공통 로직을 재사용합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/portfolio")
public class PortfolioController extends BaseController {

    private final PortfolioService portfolioService;

    /**
     * 생성자 주입
     *
     * @param userService      사용자 서비스
     * @param menuService      메뉴 서비스
     * @param portfolioService 포트폴리오 서비스
     */
    public PortfolioController(UserService userService,
                               MenuService menuService,
                               PortfolioService portfolioService) {
        super(userService, menuService);
        this.portfolioService = portfolioService;
    }

    /**
     * 포트폴리오 목록 페이지
     *
     * 사용자의 모든 포트폴리오를 조회하고 총 투자금액, 평가금액, 손익을 계산합니다.
     *
     * @param authentication Spring Security 인증 객체
     * @param model          뷰에 전달할 모델
     * @return 포트폴리오 뷰 이름
     */
    @GetMapping
    public String portfolio(Authentication authentication, Model model) {
        log.info("포트폴리오 목록 조회 - 사용자: {}", authentication.getName());

        // 공통 속성 추가 (user, menus, isAdmin)
        addCommonAttributes(model, authentication);

        // 현재 사용자 조회
        User user = getCurrentUser(authentication);

        // 사용자의 포트폴리오 목록 조회
        List<Portfolio> portfolios = portfolioService.getPortfoliosByUser(user);

        // Portfolio를 PortfolioView로 변환 (계산된 값 포함)
        List<PortfolioView> portfolioViews = portfolios.stream()
                .map(PortfolioView::from)
                .collect(Collectors.toList());

        // 총 투자금액 계산
        double totalInvestment = portfolioViews.stream()
                .mapToDouble(PortfolioView::getTotalInvestment)
                .sum();

        // 총 평가금액 계산
        double totalEvaluation = portfolioViews.stream()
                .mapToDouble(PortfolioView::getEvaluationAmount)
                .sum();

        // 총 손익 및 수익률 계산
        double totalProfitLoss = totalEvaluation - totalInvestment;
        double totalProfitLossRate = totalInvestment > 0 ? (totalProfitLoss / totalInvestment * 100) : 0;

        log.debug("포트폴리오 통계 - 투자금액: {}, 평가금액: {}, 손익: {}, 수익률: {}%",
                totalInvestment, totalEvaluation, totalProfitLoss, totalProfitLossRate);

        // 모델에 포트폴리오 데이터 추가
        model.addAttribute("portfolios", portfolioViews);
        model.addAttribute("totalInvestment", totalInvestment);
        model.addAttribute("totalEvaluation", totalEvaluation);
        model.addAttribute("totalProfitLoss", totalProfitLoss);
        model.addAttribute("totalProfitLossRate", totalProfitLossRate);

        return "portfolio";
    }

    /**
     * 포트폴리오 추가
     *
     * 새로운 포트폴리오를 추가합니다. 입력값은 DTO로 받아 검증합니다.
     *
     * @param request            포트폴리오 생성 요청 DTO
     * @param bindingResult      입력 검증 결과
     * @param authentication     Spring Security 인증 객체
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 리다이렉트 경로
     */
    @PostMapping("/add")
    public String addPortfolio(@Valid @ModelAttribute PortfolioRequest request,
                               BindingResult bindingResult,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        log.info("포트폴리오 추가 요청 - 사용자: {}, 주식: {}",
                authentication.getName(), request.getStockCode());

        // 입력 검증 실패 시
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.warn("포트폴리오 추가 검증 실패: {}", errorMessage);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/portfolio";
        }

        // 현재 사용자 조회
        User user = getCurrentUser(authentication);

        // 포트폴리오 생성
        portfolioService.createPortfolio(request, user);

        redirectAttributes.addFlashAttribute("success", "포트폴리오가 추가되었습니다.");
        return "redirect:/portfolio";
    }

    /**
     * 포트폴리오 삭제
     *
     * 지정된 ID의 포트폴리오를 삭제합니다.
     * 소유자이거나 관리자만 삭제할 수 있습니다.
     *
     * @param id                 삭제할 포트폴리오 ID
     * @param authentication     Spring Security 인증 객체
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 리다이렉트 경로
     */
    @PostMapping("/delete/{id}")
    public String deletePortfolio(@PathVariable Long id,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        log.info("포트폴리오 삭제 요청 - 사용자: {}, 포트폴리오 ID: {}",
                authentication.getName(), id);

        // 현재 사용자 및 관리자 권한 확인
        User user = getCurrentUser(authentication);
        boolean isAdmin = isAdmin(authentication);

        // 포트폴리오 삭제 (서비스에서 권한 검증)
        portfolioService.deletePortfolio(id, user, isAdmin);

        redirectAttributes.addFlashAttribute("success", "포트폴리오가 삭제되었습니다.");
        return "redirect:/portfolio";
    }
}
