package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 사용자 엔티티
 *
 * 시스템 사용자의 정보를 관리하는 엔티티입니다.
 * Spring Security 인증에 사용되며, 역할(Role) 기반 권한 관리를 지원합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 기본 키 (자동 증가)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 로그인 ID (유일값)
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 암호화된 비밀번호 (BCrypt)
     */
    @Column(nullable = false)
    private String password;

    /**
     * 사용자 실명
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 이메일 주소 (선택)
     */
    @Column(length = 100)
    private String email;

    /**
     * 계정 활성화 여부
     * false인 경우 로그인 불가
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * 계정 생성일시
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 사용자가 가진 역할(권한) 목록
     * Eager 로딩으로 설정하여 사용자 조회 시 역할도 함께 조회
     * Many-to-Many 관계로 하나의 사용자는 여러 역할을 가질 수 있음
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
