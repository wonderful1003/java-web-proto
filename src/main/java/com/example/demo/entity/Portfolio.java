package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 포트폴리오 엔티티
 *
 * 사용자가 보유한 주식 정보를 관리하는 엔티티입니다.
 * 각 사용자는 여러 개의 포트폴리오를 가질 수 있으며,
 * 각 포트폴리오는 하나의 주식 종목에 대한 정보를 담고 있습니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Entity
@Table(name = "portfolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
