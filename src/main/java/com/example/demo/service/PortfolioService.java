package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.PortfolioRequest;
import com.example.demo.entity.Portfolio;
import com.example.demo.entity.User;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 포트폴리오 비즈니스 로직 서비스
 *
 * 포트폴리오 관련 모든 비즈니스 로직을 처리합니다:
 * - 포트폴리오 조회 (전체 목록, 단건)
 * - 포트폴리오 생성
 * - 포트폴리오 삭제
 * - 권한 검증
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    /**
     * 사용자의 모든 포트폴리오 조회
     *
     * @param user 조회할 사용자
     * @return 포트폴리오 목록 (생성일 내림차순)
     */
    public List<Portfolio> getPortfoliosByUser(User user) {
        log.info("사용자({})의 포트폴리오 목록 조회", user.getUsername());
        return portfolioRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * ID로 포트폴리오 조회
     *
     * @param id 포트폴리오 ID
     * @return 조회된 포트폴리오
     * @throws ResourceNotFoundException 포트폴리오를 찾을 수 없을 때
     */
    public Portfolio getPortfolioById(Long id) {
        log.debug("포트폴리오 조회: ID={}", id);
        return portfolioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("포트폴리오를 찾을 수 없음: ID={}", id);
                    return new ResourceNotFoundException(ErrorMessages.PORTFOLIO_NOT_FOUND);
                });
    }

    /**
     * 새로운 포트폴리오 생성
     *
     * @param request 포트폴리오 생성 요청 DTO
     * @param user    포트폴리오 소유자
     * @return 생성된 포트폴리오
     */
    @Transactional
    public Portfolio createPortfolio(PortfolioRequest request, User user) {
        log.info("포트폴리오 생성 - 사용자: {}, 주식: {}", user.getUsername(), request.getStockCode());

        // 총 투자금액 계산
        double totalInvestment = request.getQuantity() * request.getAveragePrice();

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .stockCode(request.getStockCode())
                .stockName(request.getStockName())
                .quantity(request.getQuantity())
                .averagePrice(request.getAveragePrice())
                .totalInvestment(totalInvestment)
                .currentPrice(request.getAveragePrice())  // 초기값은 평균가로 설정
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Portfolio saved = portfolioRepository.save(portfolio);
        log.info("포트폴리오 생성 완료: ID={}, 총 투자금액: {}", saved.getId(), totalInvestment);
        return saved;
    }

    /**
     * 포트폴리오 삭제 (권한 검증 포함)
     *
     * @param id          삭제할 포트폴리오 ID
     * @param currentUser 현재 로그인한 사용자
     * @param isAdmin     현재 사용자가 관리자인지 여부
     * @throws ResourceNotFoundException 포트폴리오를 찾을 수 없을 때
     * @throws AccessDeniedException     권한이 없을 때
     */
    @Transactional
    public void deletePortfolio(Long id, User currentUser, boolean isAdmin) {
        Portfolio portfolio = getPortfolioById(id);

        // 권한 검증: 소유자이거나 관리자만 삭제 가능
        if (!portfolio.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            log.warn("포트폴리오 삭제 권한 없음 - 포트폴리오 ID: {}, 사용자: {}",
                    id, currentUser.getUsername());
            throw new AccessDeniedException(ErrorMessages.PORTFOLIO_ACCESS_DENIED);
        }

        log.info("포트폴리오 삭제 - ID: {}, 소유자: {}, 삭제 요청자: {}",
                id, portfolio.getUser().getUsername(), currentUser.getUsername());
        portfolioRepository.delete(portfolio);
    }

    /**
     * 포트폴리오 소유자 확인
     *
     * @param portfolio   확인할 포트폴리오
     * @param currentUser 현재 사용자
     * @return 소유자이면 true, 아니면 false
     */
    public boolean isOwner(Portfolio portfolio, User currentUser) {
        boolean isOwner = portfolio.getUser().getId().equals(currentUser.getId());
        log.debug("포트폴리오 소유자 확인 - 포트폴리오 ID: {}, 소유자 여부: {}", portfolio.getId(), isOwner);
        return isOwner;
    }
}
