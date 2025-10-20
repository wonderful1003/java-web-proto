package com.example.demo.constant;

/**
 * 시스템 권한 타입 정의
 *
 * 애플리케이션에서 사용되는 모든 권한을 상수로 관리합니다.
 * 하드코딩된 문자열 사용을 방지하고 타입 안전성을 보장합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
public enum RoleType {
    /**
     * 관리자 권한
     * - 모든 관리 기능 접근 가능
     * - 사용자, 역할, 메뉴 관리 권한 포함
     */
    ADMIN("ROLE_ADMIN", "관리자"),

    /**
     * 일반 사용자 권한
     * - 기본 기능 사용 가능
     * - 대시보드, 포트폴리오, 게시판 접근 가능
     */
    USER("ROLE_USER", "사용자");

    private final String authority;
    private final String description;

    /**
     * RoleType 생성자
     *
     * @param authority Spring Security에서 사용되는 권한 문자열 (예: ROLE_ADMIN)
     * @param description 권한에 대한 한글 설명
     */
    RoleType(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    /**
     * Spring Security 권한 문자열 반환
     *
     * @return 권한 문자열 (예: "ROLE_ADMIN")
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * 권한 설명 반환
     *
     * @return 한글 설명 (예: "관리자")
     */
    public String getDescription() {
        return description;
    }

    /**
     * 권한 문자열로 RoleType 조회
     *
     * @param authority 조회할 권한 문자열 (예: "ROLE_ADMIN")
     * @return 일치하는 RoleType, 없으면 null
     */
    public static RoleType fromAuthority(String authority) {
        for (RoleType roleType : values()) {
            if (roleType.authority.equals(authority)) {
                return roleType;
            }
        }
        return null;
    }
}
