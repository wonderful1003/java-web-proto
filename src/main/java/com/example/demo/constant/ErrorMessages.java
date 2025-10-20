package com.example.demo.constant;

/**
 * 에러 메시지 상수 관리 클래스
 *
 * 애플리케이션 전체에서 사용되는 에러 메시지를 중앙에서 관리합니다.
 * 하드코딩된 에러 메시지를 방지하고 일관성을 유지합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
public final class ErrorMessages {

    // 생성자를 private으로 선언하여 인스턴스화 방지
    private ErrorMessages() {
        throw new AssertionError("상수 클래스는 인스턴스화할 수 없습니다.");
    }

    // ========== 사용자 관련 에러 메시지 ==========
    /**
     * 사용자를 찾을 수 없을 때 사용
     */
    public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";

    /**
     * 중복된 사용자명이 존재할 때 사용
     */
    public static final String USER_ALREADY_EXISTS = "이미 존재하는 사용자명입니다.";

    /**
     * 사용자 생성 실패 시 사용
     */
    public static final String USER_CREATE_FAILED = "사용자 생성에 실패했습니다.";

    // ========== 권한 관련 에러 메시지 ==========
    /**
     * 역할(Role)을 찾을 수 없을 때 사용
     */
    public static final String ROLE_NOT_FOUND = "역할을 찾을 수 없습니다.";

    /**
     * 권한이 부족할 때 사용
     */
    public static final String ACCESS_DENIED = "접근 권한이 없습니다.";

    // ========== 포트폴리오 관련 에러 메시지 ==========
    /**
     * 포트폴리오를 찾을 수 없을 때 사용
     */
    public static final String PORTFOLIO_NOT_FOUND = "포트폴리오를 찾을 수 없습니다.";

    /**
     * 포트폴리오 소유자가 아닐 때 사용
     */
    public static final String PORTFOLIO_ACCESS_DENIED = "해당 포트폴리오에 접근할 권한이 없습니다.";

    // ========== 계산 기록 관련 에러 메시지 ==========
    /**
     * 계산 기록을 찾을 수 없을 때 사용
     */
    public static final String HISTORY_NOT_FOUND = "계산 기록을 찾을 수 없습니다.";

    /**
     * 계산 기록 소유자가 아닐 때 사용
     */
    public static final String HISTORY_ACCESS_DENIED = "해당 계산 기록에 접근할 권한이 없습니다.";

    // ========== 게시판 관련 에러 메시지 ==========
    /**
     * 게시글을 찾을 수 없을 때 사용
     */
    public static final String BOARD_NOT_FOUND = "게시글을 찾을 수 없습니다.";

    /**
     * 게시글 작성자가 아니고 관리자도 아닐 때 사용
     */
    public static final String BOARD_ACCESS_DENIED = "해당 게시글에 접근할 권한이 없습니다.";

    // ========== 메뉴 관련 에러 메시지 ==========
    /**
     * 메뉴를 찾을 수 없을 때 사용
     */
    public static final String MENU_NOT_FOUND = "메뉴를 찾을 수 없습니다.";

    // ========== 입력 검증 에러 메시지 ==========
    /**
     * 필수 입력값이 없을 때 사용
     */
    public static final String REQUIRED_FIELD_MISSING = "필수 입력값이 누락되었습니다.";

    /**
     * 입력값이 유효하지 않을 때 사용
     */
    public static final String INVALID_INPUT = "유효하지 않은 입력값입니다.";

    /**
     * 주식 코드가 비어있을 때 사용
     */
    public static final String STOCK_CODE_REQUIRED = "주식 코드는 필수입니다.";

    /**
     * 수량이 양수가 아닐 때 사용
     */
    public static final String QUANTITY_MUST_BE_POSITIVE = "수량은 양수여야 합니다.";

    /**
     * 가격이 양수가 아닐 때 사용
     */
    public static final String PRICE_MUST_BE_POSITIVE = "가격은 양수여야 합니다.";
}
