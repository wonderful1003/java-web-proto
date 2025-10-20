package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 평단가 계산 기록 엔티티
 *
 * 사용자의 평단가(평균 단가) 계산 기록을 저장하는 엔티티입니다.
 * 추가 매수 시 새로운 평균 매수가를 계산하고 기록을 남깁니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Entity
@Table(name = "calculation_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculationHistory {

    /**
     * 기본 키 (자동 증가)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 계산 기록 소유자 (사용자)
     * 지연 로딩으로 설정
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 주식 코드 (예: "005930" - 삼성전자)
     */
    @Column(nullable = false, length = 20)
    private String stockCode;

    /**
     * 주식명 (예: "삼성전자")
     */
    @Column(nullable = false, length = 100)
    private String stockName;

    /**
     * 기존 보유 수량
     */
    @Column(nullable = false)
    private Integer existingQuantity;

    /**
     * 기존 평균 매수가
     */
    @Column(nullable = false)
    private Double existingAvgPrice;

    /**
     * 추가 매수 수량
     */
    @Column(nullable = false)
    private Integer additionalQuantity;

    /**
     * 추가 매수가
     */
    @Column(nullable = false)
    private Double additionalPrice;

    /**
     * 계산된 새로운 평균 매수가
     */
    @Column(nullable = false)
    private Double newAveragePrice;

    /**
     * 새로운 총 수량 (기존 수량 + 추가 수량)
     */
    @Column(nullable = false)
    private Integer newTotalQuantity;

    /**
     * 기록 생성일시
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
