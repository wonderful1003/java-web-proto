package com.example.demo.config;

import com.example.demo.entity.Portfolio;
import com.example.demo.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 포트폴리오 데이터 마이그레이션
 *
 * 기존 포트폴리오 데이터에 totalInvestment가 null인 경우 자동으로 계산하여 업데이트합니다.
 * 애플리케이션 시작 시 한 번만 실행됩니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PortfolioDataMigration implements CommandLineRunner {

    private final PortfolioRepository portfolioRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("=== 포트폴리오 데이터 마이그레이션 시작 ===");

        // totalInvestment가 null인 포트폴리오 조회
        List<Portfolio> allPortfolios = portfolioRepository.findAll();
        int updatedCount = 0;

        for (Portfolio portfolio : allPortfolios) {
            boolean needsUpdate = false;

            // totalInvestment가 null이면 계산
            if (portfolio.getTotalInvestment() == null) {
                double totalInvestment = portfolio.getQuantity() * portfolio.getAveragePrice();
                portfolio.setTotalInvestment(totalInvestment);
                needsUpdate = true;
                log.debug("포트폴리오 ID {} - totalInvestment 계산: {}", portfolio.getId(), totalInvestment);
            }

            // currentPrice가 null이면 averagePrice로 설정
            if (portfolio.getCurrentPrice() == null) {
                portfolio.setCurrentPrice(portfolio.getAveragePrice());
                needsUpdate = true;
                log.debug("포트폴리오 ID {} - currentPrice 설정: {}", portfolio.getId(), portfolio.getAveragePrice());
            }

            // updatedAt이 null이면 현재 시간으로 설정
            if (portfolio.getUpdatedAt() == null) {
                portfolio.setUpdatedAt(LocalDateTime.now());
                needsUpdate = true;
            }

            if (needsUpdate) {
                portfolioRepository.save(portfolio);
                updatedCount++;
            }
        }

        if (updatedCount > 0) {
            log.info("=== 포트폴리오 데이터 마이그레이션 완료: {}개 업데이트 ===", updatedCount);
        } else {
            log.info("=== 포트폴리오 데이터 마이그레이션 완료: 업데이트 필요 없음 ===");
        }
    }
}
