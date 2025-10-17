package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.Portfolio;
import com.example.demo.entity.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 포트폴리오 컨트롤러
 * 사용자의 주식 포트폴리오 관리
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final UserService userService;
    private final MenuService menuService;
    private final PortfolioRepository portfolioRepository;

    @GetMapping
    public String portfolio(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        List<Portfolio> portfolios = portfolioRepository.findByUser_UsernameOrderByCreatedAtDesc(username);

        // 총 투자금액 및 평가금액 계산
        double totalInvestment = portfolios.stream()
                .mapToDouble(Portfolio::getTotalInvestment)
                .sum();

        double totalEvaluation = portfolios.stream()
                .mapToDouble(p -> (p.getCurrentPrice() != null ? p.getCurrentPrice() : p.getAveragePrice()) * p.getQuantity())
                .sum();

        double totalProfitLoss = totalEvaluation - totalInvestment;
        double totalProfitLossRate = totalInvestment > 0 ? (totalProfitLoss / totalInvestment * 100) : 0;

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("totalInvestment", totalInvestment);
        model.addAttribute("totalEvaluation", totalEvaluation);
        model.addAttribute("totalProfitLoss", totalProfitLoss);
        model.addAttribute("totalProfitLossRate", totalProfitLossRate);

        return "portfolio";
    }

    @PostMapping("/add")
    public String addPortfolio(@RequestParam String stockName,
                              @RequestParam String stockCode,
                              @RequestParam Integer quantity,
                              @RequestParam Double averagePrice,
                              Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setStockName(stockName);
        portfolio.setStockCode(stockCode);
        portfolio.setQuantity(quantity);
        portfolio.setAveragePrice(averagePrice);
        portfolio.setTotalInvestment(quantity * averagePrice);
        portfolio.setCurrentPrice(averagePrice);  // 초기값
        portfolio.setCreatedAt(LocalDateTime.now());
        portfolio.setUpdatedAt(LocalDateTime.now());

        portfolioRepository.save(portfolio);

        return "redirect:/portfolio";
    }

    @PostMapping("/delete/{id}")
    public String deletePortfolio(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        // 본인 포트폴리오만 삭제 가능
        if (portfolio.getUser().getUsername().equals(username)) {
            portfolioRepository.delete(portfolio);
        }

        return "redirect:/portfolio";
    }
}
