package com.example.demo.exception;

/**
 * 비즈니스 로직 예외의 최상위 클래스
 *
 * 애플리케이션의 모든 비즈니스 예외는 이 클래스를 상속받습니다.
 * RuntimeException을 상속하여 unchecked exception으로 동작합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
public class BusinessException extends RuntimeException {

    /**
     * 기본 생성자
     */
    public BusinessException() {
        super();
    }

    /**
     * 메시지를 포함한 생성자
     *
     * @param message 예외 메시지
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외를 포함한 생성자
     *
     * @param message 예외 메시지
     * @param cause   원인 예외
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 원인 예외를 포함한 생성자
     *
     * @param cause 원인 예외
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }
}
