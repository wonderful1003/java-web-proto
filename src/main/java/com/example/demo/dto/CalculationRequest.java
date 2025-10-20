package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 평단가 계산 요청 DTO
 *
 * 클라이언트로부터 평단가 계산 정보를 받을 때 사용합니다.
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
public class CalculationRequest {

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
     * 기존 보유 수량
     * 0 또는 양수 (처음 매수하는 경우 0)
     */
    @PositiveOrZero(message = "기존 수량은 0 이상이어야 합니다.")
    private Integer existingQuantity;

    /**
     * 기존 평균 매수가
     * 0 또는 양수 (처음 매수하는 경우 0)
     */
    @PositiveOrZero(message = "기존 평균가는 0 이상이어야 합니다.")
    private Double existingAvgPrice;

    /**
     * 추가 매수 수량
     * 양수만 입력 가능
     */
    @Positive(message = "추가 수량은 양수여야 합니다.")
    private Integer additionalQuantity;

    /**
     * 추가 매수가
     * 양수만 입력 가능
     */
    @Positive(message = "추가 매수가는 양수여야 합니다.")
    private Double additionalPrice;
}
