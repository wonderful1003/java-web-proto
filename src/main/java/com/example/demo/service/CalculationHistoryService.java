package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.CalculationRequest;
import com.example.demo.entity.CalculationHistory;
import com.example.demo.entity.User;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CalculationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 평단가 계산 기록 비즈니스 로직 서비스
 *
 * 평단가 계산 기록 관련 모든 비즈니스 로직을 처리합니다:
 * - 계산 기록 조회 (전체 목록, 단건)
 * - 평단가 계산 및 기록 생성
 * - 계산 기록 삭제
 * - 권한 검증
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalculationHistoryService {

    private final CalculationHistoryRepository calculationHistoryRepository;

    /**
     * 사용자의 모든 계산 기록 조회
     *
     * @param user 조회할 사용자
     * @return 계산 기록 목록 (생성일 내림차순)
     */
    public List<CalculationHistory> getHistoriesByUser(User user) {
        log.info("사용자({})의 계산 기록 목록 조회", user.getUsername());
        return calculationHistoryRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * ID로 계산 기록 조회
     *
     * @param id 계산 기록 ID
     * @return 조회된 계산 기록
     * @throws ResourceNotFoundException 계산 기록을 찾을 수 없을 때
     */
    public CalculationHistory getHistoryById(Long id) {
        log.debug("계산 기록 조회: ID={}", id);
        return calculationHistoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("계산 기록을 찾을 수 없음: ID={}", id);
                    return new ResourceNotFoundException(ErrorMessages.HISTORY_NOT_FOUND);
                });
    }

    /**
     * 평단가 계산 및 기록 생성
     *
     * 기존 보유 수량과 추가 매수 수량을 합산하여 새로운 평단가를 계산합니다.
     * 계산 공식: (기존 수량 × 기존 평균가 + 추가 수량 × 추가 매수가) ÷ (기존 수량 + 추가 수량)
     *
     * @param request 계산 요청 DTO
     * @param user    계산 요청 사용자
     * @return 생성된 계산 기록
     */
    @Transactional
    public CalculationHistory calculateAndSave(CalculationRequest request, User user) {
        log.info("평단가 계산 시작 - 사용자: {}, 주식: {}", user.getUsername(), request.getStockCode());

        // 평단가 계산
        double totalCost = (request.getExistingQuantity() * request.getExistingAvgPrice())
                + (request.getAdditionalQuantity() * request.getAdditionalPrice());
        int totalQuantity = request.getExistingQuantity() + request.getAdditionalQuantity();
        double newAveragePrice = totalCost / totalQuantity;

        log.debug("평단가 계산 완료 - 총 비용: {}, 총 수량: {}, 새 평단가: {}",
                totalCost, totalQuantity, newAveragePrice);

        // 계산 기록 생성
        CalculationHistory history = new CalculationHistory();
        history.setUser(user);
        history.setStockCode(request.getStockCode());
        history.setStockName(request.getStockName());
        history.setExistingQuantity(request.getExistingQuantity());
        history.setExistingAvgPrice(request.getExistingAvgPrice());
        history.setAdditionalQuantity(request.getAdditionalQuantity());
        history.setAdditionalPrice(request.getAdditionalPrice());
        history.setNewAveragePrice(newAveragePrice);
        history.setNewTotalQuantity(totalQuantity);
        history.setCreatedAt(LocalDateTime.now());

        CalculationHistory saved = calculationHistoryRepository.save(history);
        log.info("계산 기록 저장 완료: ID={}", saved.getId());
        return saved;
    }

    /**
     * 계산 기록 삭제 (권한 검증 포함)
     *
     * @param id          삭제할 계산 기록 ID
     * @param currentUser 현재 로그인한 사용자
     * @param isAdmin     현재 사용자가 관리자인지 여부
     * @throws ResourceNotFoundException 계산 기록을 찾을 수 없을 때
     * @throws AccessDeniedException     권한이 없을 때
     */
    @Transactional
    public void deleteHistory(Long id, User currentUser, boolean isAdmin) {
        CalculationHistory history = getHistoryById(id);

        // 권한 검증: 소유자이거나 관리자만 삭제 가능
        if (!history.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            log.warn("계산 기록 삭제 권한 없음 - 기록 ID: {}, 사용자: {}",
                    id, currentUser.getUsername());
            throw new AccessDeniedException(ErrorMessages.HISTORY_ACCESS_DENIED);
        }

        log.info("계산 기록 삭제 - ID: {}, 소유자: {}, 삭제 요청자: {}",
                id, history.getUser().getUsername(), currentUser.getUsername());
        calculationHistoryRepository.delete(history);
    }

    /**
     * 계산 기록 소유자 확인
     *
     * @param history     확인할 계산 기록
     * @param currentUser 현재 사용자
     * @return 소유자이면 true, 아니면 false
     */
    public boolean isOwner(CalculationHistory history, User currentUser) {
        boolean isOwner = history.getUser().getId().equals(currentUser.getId());
        log.debug("계산 기록 소유자 확인 - 기록 ID: {}, 소유자 여부: {}", history.getId(), isOwner);
        return isOwner;
    }
}
