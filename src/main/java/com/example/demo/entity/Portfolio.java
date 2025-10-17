package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 포트폴리오 엔티티
 * 사용자의 주식 보유 내역 관리
 */
@Entity
@Table(name = "portfolios")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String stockName;  // 종목명

    @Column(nullable = false, length = 20)
    private String stockCode;  // 종목코드

    @Column(nullable = false)
    private Integer quantity;  // 보유 수량

    @Column(nullable = false)
    private Double averagePrice;  // 평균 매수가

    @Column(nullable = false)
    private Double totalInvestment;  // 총 투자금액

    @Column
    private Double currentPrice;  // 현재가

    @Column
    private Double profitLoss;  // 손익

    @Column
    private Double profitLossRate;  // 수익률

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
