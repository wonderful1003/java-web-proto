package com.example.demo.dto;

import com.example.demo.entity.Portfolio;
import lombok.Getter;
import lombok.Setter;

/**
 * 포트폴리오 뷰 DTO
 *
 * 템플릿에서 사용할 계산된 값들을 포함하는 DTO입니다.
 * 복잡한 Thymeleaf 표현식을 피하고 Controller에서 계산한 값을 전달합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Getter
@Setter
public class PortfolioView {

    private Long id;
    private String stockName;
    private String stockCode;
    private Integer quantity;
    private Double averagePrice;
    private Double currentPrice;
    private Double totalInvestment;    // 총 투자금액
    private Double evaluationAmount;   // 평가금액
    private Double profitLoss;         // 손익
    private Double profitLossRate;     // 수익률 (%)

    /**
     * Portfolio 엔티티로부터 PortfolioView 생성
     *
     * @param portfolio 포트폴리오 엔티티
     * @return 계산된 값이 포함된 PortfolioView
     */
    public static PortfolioView from(Portfolio portfolio) {
        PortfolioView view = new PortfolioView();

        view.setId(portfolio.getId());
        view.setStockName(portfolio.getStockName());
        view.setStockCode(portfolio.getStockCode());
        view.setQuantity(portfolio.getQuantity());
        view.setAveragePrice(portfolio.getAveragePrice());

        // currentPrice가 null이면 averagePrice 사용
        double currentPrice = portfolio.getCurrentPrice() != null
            ? portfolio.getCurrentPrice()
            : portfolio.getAveragePrice();
        view.setCurrentPrice(currentPrice);

        // totalInvestment가 null이면 계산
        double totalInvestment = portfolio.getTotalInvestment() != null
            ? portfolio.getTotalInvestment()
            : (portfolio.getQuantity() * portfolio.getAveragePrice());
        view.setTotalInvestment(totalInvestment);

        // 평가금액 계산
        double evaluationAmount = currentPrice * portfolio.getQuantity();
        view.setEvaluationAmount(evaluationAmount);

        // 손익 계산
        double profitLoss = evaluationAmount - totalInvestment;
        view.setProfitLoss(profitLoss);

        // 수익률 계산 (%)
        double profitLossRate = totalInvestment > 0
            ? (profitLoss / totalInvestment * 100)
            : 0.0;
        view.setProfitLossRate(profitLossRate);

        return view;
    }
}
