package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 계산 히스토리 엔티티
 * 사용자의 물타기 계산 기록 저장
 */
@Entity
@Table(name = "calculation_history")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String calculationType;  // FORWARD, REVERSE

    @Column(length = 100)
    private String stockName;  // 종목명 (선택)

    @Column
    private Double oldPrice;  // 기존 매수 단가

    @Column
    private Integer oldQuantity;  // 기존 보유 수량

    @Column
    private Double newPrice;  // 추가 매수 단가

    @Column
    private Integer newQuantity;  // 추가 매수 수량

    @Column
    private Double averagePrice;  // 계산된 평균 단가

    @Column
    private Integer totalQuantity;  // 총 수량

    @Column
    private Double totalAmount;  // 총 투자금액

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
