package com.example.demo.exception;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 *
 * 데이터베이스에서 특정 엔티티를 조회했으나 존재하지 않을 때 사용합니다.
 * (예: 사용자, 포트폴리오, 게시글 등)
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
public class ResourceNotFoundException extends BusinessException {

    /**
     * 메시지를 포함한 생성자
     *
     * @param message 예외 메시지 (예: "사용자를 찾을 수 없습니다.")
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * 리소스 타입과 ID를 포함한 생성자
     *
     * @param resourceType 리소스 타입 (예: "User", "Portfolio")
     * @param resourceId   리소스 ID
     */
    public ResourceNotFoundException(String resourceType, Long resourceId) {
        super(String.format("%s(ID: %d)을(를) 찾을 수 없습니다.", resourceType, resourceId));
    }

    /**
     * 리소스 타입과 식별자를 포함한 생성자
     *
     * @param resourceType 리소스 타입 (예: "User", "Portfolio")
     * @param identifier   식별자 (예: username, stockCode)
     */
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s(%s)을(를) 찾을 수 없습니다.", resourceType, identifier));
    }
}
