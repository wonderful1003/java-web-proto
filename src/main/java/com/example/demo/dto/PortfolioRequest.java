package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 포트폴리오 생성/수정 요청 DTO
 *
 * 클라이언트로부터 포트폴리오 정보를 받을 때 사용합니다.
 * Bean Validation을 통해 입력값을 검증합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioRequest {

    /**
     * 주식 코드 (예: "005930" - 삼성전자)
     * 필수 입력값
     */
    @NotBlank(message = "주식 코드는 필수입니다.")
    private String stockCode;

    /**
     * 주식명 (예: "삼성전자")
     * 필수 입력값
     */
    @NotBlank(message = "주식명은 필수입니다.")
    private String stockName;

    /**
     * 보유 수량
     * 양수만 입력 가능
     */
    @Positive(message = "수량은 양수여야 합니다.")
    private Integer quantity;

    /**
     * 평균 매수가
     * 양수만 입력 가능
     */
    @Positive(message = "평균가는 양수여야 합니다.")
    private Double averagePrice;
}
