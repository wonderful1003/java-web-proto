package com.example.demo.exception;

/**
 * 접근 권한이 없을 때 발생하는 예외
 *
 * 사용자가 리소스에 접근할 권한이 없을 때 사용합니다.
 * (예: 다른 사용자의 포트폴리오 수정 시도, 관리자 전용 기능 접근 등)
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
public class AccessDeniedException extends BusinessException {

    /**
     * 메시지를 포함한 생성자
     *
     * @param message 예외 메시지 (예: "접근 권한이 없습니다.")
     */
    public AccessDeniedException(String message) {
        super(message);
    }

    /**
     * 기본 생성자
     * 기본 메시지를 사용합니다.
     */
    public AccessDeniedException() {
        super("접근 권한이 없습니다.");
    }
}
