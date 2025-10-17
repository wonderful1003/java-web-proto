# 터틀맨 타운 (Turtleman Town) - 아키텍처 설계서

## 📋 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [시스템 아키텍처](#시스템-아키텍처)
4. [패키지 구조](#패키지-구조)
5. [핵심 컴포넌트](#핵심-컴포넌트)
6. [데이터베이스 설계](#데이터베이스-설계)
7. [보안 설계](#보안-설계)
8. [API 엔드포인트](#api-엔드포인트)
9. [개발 및 배포](#개발-및-배포)

---

## 프로젝트 개요

**터틀맨 타운**은 주식 평균 매수 단가 계산 및 투자 관리를 위한 웹 애플리케이션입니다.

### 주요 기능
- 🔐 **사용자 인증 및 권한 관리** (Spring Security)
- 📊 **물타기 계산기** (평균 단가 계산 & 목표 평단가 역산)
- 👥 **역할 기반 접근 제어** (RBAC - Admin/User)
- 📱 **반응형 웹 UI** (Thymeleaf + CSS)
- 🗄️ **다중 데이터베이스 지원** (H2/MySQL/PostgreSQL)

---

## 기술 스택

### Backend
- **Framework**: Spring Boot 3.3.5
- **Language**: Java 17
- **Security**: Spring Security 6
- **ORM**: Spring Data JPA / Hibernate
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven

### Database
- **Local Development**: H2 (In-Memory)
- **Development**: MySQL 8.x
- **Production**: PostgreSQL 14+

### Frontend
- **Template**: Thymeleaf
- **Styling**: Vanilla CSS
- **JavaScript**: ES6+ (Vanilla JS)

### DevOps
- **Container**: Docker (optional)
- **Deployment**: Render / Cloud Run / Heroku

---

## 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │  Thymeleaf   │  │   Static     │  │     CSS      │ │
│  │  Templates   │  │   Resources  │  │  JavaScript  │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                    Controller Layer                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │     Auth     │  │   Dashboard  │  │     Web      │ │
│  │  Controller  │  │  Controller  │  │  Controller  │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                     Service Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │     User     │  │     Menu     │  │   Security   │ │
│  │   Service    │  │   Service    │  │  UserDetails │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                   Repository Layer                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │     User     │  │     Role     │  │     Menu     │ │
│  │  Repository  │  │  Repository  │  │  Repository  │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                     Data Layer                          │
│         H2 / MySQL / PostgreSQL Database                │
└─────────────────────────────────────────────────────────┘
```

---

## 패키지 구조

```
src/main/java/com/example/demo/
├── config/                     # 설정 클래스
│   ├── SecurityConfig.java    # Spring Security 설정
│   └── DataLoader.java         # 초기 데이터 로더
│
├── controller/                 # 컨트롤러 레이어
│   ├── AuthController.java    # 인증 관련 (로그인)
│   ├── DashboardController.java # 대시보드
│   └── WebController.java     # 메인 페이지 라우팅
│
├── entity/                     # JPA 엔티티
│   ├── User.java              # 사용자 엔티티
│   ├── Role.java              # 역할 엔티티
│   └── Menu.java              # 메뉴 엔티티
│
├── repository/                 # 데이터 액세스 레이어
│   ├── UserRepository.java    # 사용자 Repository
│   ├── RoleRepository.java    # 역할 Repository
│   └── MenuRepository.java    # 메뉴 Repository
│
├── service/                    # 비즈니스 로직 레이어
│   ├── UserService.java       # 사용자 서비스
│   └── MenuService.java       # 메뉴 서비스
│
├── security/                   # 보안 관련
│   └── CustomUserDetailsService.java # 인증 처리
│
└── DemoApplication.java        # 메인 애플리케이션

src/main/resources/
├── static/                     # 정적 리소스
│   ├── css/                   # 스타일시트
│   │   ├── login.css
│   │   ├── dashboard.css
│   │   └── main.css
│   ├── js/                    # JavaScript
│   │   └── main.js
│   └── index.html             # 계산기 페이지
│
├── templates/                  # Thymeleaf 템플릿
│   ├── login.html             # 로그인 페이지
│   └── dashboard.html         # 대시보드 페이지
│
└── application.properties      # 애플리케이션 설정
```

---

## 핵심 컴포넌트

### 1. Controller Layer

#### AuthController
```java
/**
 * 인증 관련 컨트롤러
 * - 로그인 페이지 제공
 * - 로그인 실패/로그아웃 메시지 처리
 */
@GetMapping("/login")
```

#### DashboardController
```java
/**
 * 대시보드 컨트롤러
 * - 사용자별 메뉴 조회
 * - 권한별 대시보드 렌더링
 */
@GetMapping("/dashboard")
```

#### WebController
```java
/**
 * 메인 라우팅 컨트롤러
 * - 루트 경로 → 로그인 페이지
 * - 계산기 경로 → 계산기 페이지
 */
@GetMapping("/"), @GetMapping("/calculator")
```

### 2. Service Layer

#### UserService
- 사용자 CRUD 작업
- 비밀번호 암호화 (BCrypt)
- 역할 할당 및 관리

#### MenuService
- 사용자 권한에 따른 메뉴 필터링
- 메뉴 계층 구조 관리

#### CustomUserDetailsService
- Spring Security 인증 처리
- 데이터베이스 사용자 정보 조회
- GrantedAuthority 변환

### 3. Repository Layer

#### UserRepository
```java
Optional<User> findByUsername(String username);
boolean existsByUsername(String username);
```

#### RoleRepository
```java
Optional<Role> findByName(String name);
```

#### MenuRepository
```java
List<Menu> findByVisibleTrueOrderBySortOrder();
@Query("SELECT DISTINCT m FROM Menu m JOIN m.roles r WHERE r.name IN :roleNames AND m.visible = true ORDER BY m.sortOrder")
List<Menu> findByRoleNamesAndVisible(@Param("roleNames") List<String> roleNames);
```

---

## 데이터베이스 설계

### ERD (Entity Relationship Diagram)

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│    User     │         │    Role     │         │    Menu     │
├─────────────┤         ├─────────────┤         ├─────────────┤
│ id (PK)     │         │ id (PK)     │         │ id (PK)     │
│ username    │────────>│ name        │<────────│ name        │
│ password    │  M:N    │ description │   M:N   │ path        │
│ name        │         │             │         │ icon        │
│ email       │         └─────────────┘         │ parent_id   │
│ enabled     │                                 │ sort_order  │
│ created_at  │                                 │ visible     │
└─────────────┘                                 └─────────────┘
       │                                               │
       └──────────────> user_roles <──────────────────┘
                        ┌──────────┐
                        │ user_id  │
                        │ role_id  │
                        └──────────┘
                              │
                              │
                        menu_roles
                        ┌──────────┐
                        │ role_id  │
                        │ menu_id  │
                        └──────────┘
```

### 테이블 설명

#### users
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 사용자 ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 로그인 아이디 |
| password | VARCHAR(255) | NOT NULL | 암호화된 비밀번호 |
| name | VARCHAR(100) | NOT NULL | 사용자 이름 |
| email | VARCHAR(100) | - | 이메일 |
| enabled | BOOLEAN | NOT NULL, DEFAULT true | 계정 활성화 여부 |
| created_at | TIMESTAMP | - | 생성일시 |

#### roles
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 역할 ID |
| name | VARCHAR(50) | UNIQUE, NOT NULL | 역할명 (ROLE_ADMIN, ROLE_USER) |
| description | VARCHAR(200) | - | 역할 설명 |

#### menus
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 메뉴 ID |
| name | VARCHAR(100) | NOT NULL | 메뉴명 |
| path | VARCHAR(200) | - | URL 경로 |
| icon | VARCHAR(50) | - | 아이콘 (이모지) |
| parent_id | BIGINT | FK (self) | 부모 메뉴 ID |
| sort_order | INT | DEFAULT 0 | 정렬 순서 |
| visible | BOOLEAN | NOT NULL, DEFAULT true | 표시 여부 |

---

## 보안 설계

### Spring Security 설정

#### 인증 방식
- **Form Login**: Username/Password 기반 인증
- **BCrypt**: 비밀번호 암호화 (Strength 10)
- **Session**: 세션 기반 인증 유지

#### 권한 구조
```java
ROLE_ADMIN: 관리자 권한 (모든 기능 접근)
ROLE_USER:  일반 사용자 권한 (제한된 기능)
```

#### 접근 제어 (URL 기반)
```java
"/", "/login": 모든 사용자 허용
"/h2-console/**": 모든 사용자 허용 (개발 전용)
"/css/**", "/js/**": 정적 리소스 허용
"/admin/**": ROLE_ADMIN만 허용
그 외 모든 경로: 인증 필요
```

#### SecurityFilterChain
```java
1. 요청 권한 검사 (authorizeHttpRequests)
2. 폼 로그인 처리 (formLogin)
   - loginPage: /login
   - loginProcessingUrl: /perform-login
   - successUrl: /dashboard
   - failureUrl: /login?error=true
3. 로그아웃 처리 (logout)
   - logoutSuccessUrl: /
4. CSRF: 비활성화 (개발 편의, 프로덕션에서는 활성화 권장)
5. Frame Options: 비활성화 (H2 콘솔용)
```

---

## API 엔드포인트

### 인증 관련
| Method | URL | Description | Auth Required |
|--------|-----|-------------|---------------|
| GET | `/login` | 로그인 페이지 | ❌ |
| POST | `/perform-login` | 로그인 처리 | ❌ |
| POST | `/logout` | 로그아웃 | ✅ |

### 페이지 라우팅
| Method | URL | Description | Auth Required | Role |
|--------|-----|-------------|---------------|------|
| GET | `/` | 루트 → 로그인 리다이렉트 | ❌ | - |
| GET | `/dashboard` | 대시보드 | ✅ | USER, ADMIN |
| GET | `/calculator` | 계산기 페이지 | ✅ | USER, ADMIN |

### 관리자 기능 (예정)
| Method | URL | Description | Auth Required | Role |
|--------|-----|-------------|---------------|------|
| GET | `/admin/users` | 회원 관리 | ✅ | ADMIN |
| GET | `/admin/roles` | 권한 관리 | ✅ | ADMIN |
| GET | `/admin/menus` | 메뉴 관리 | ✅ | ADMIN |

---

## 개발 및 배포

### 로컬 개발 환경 설정

#### 1. 필수 요구사항
- Java 17+
- Maven 3.8+
- H2 Database (내장)

#### 2. 애플리케이션 실행
```bash
# Maven으로 실행
mvn spring-boot:run

# 또는 빌드 후 실행
mvn clean package
java -jar target/java-web-proto-0.0.1-SNAPSHOT.jar
```

#### 3. 접속 정보
- **Application**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비어있음)

#### 4. 기본 계정
```
관리자: admin / admin123
일반 사용자: user / user123
```

### 환경별 설정

#### Local (H2)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

#### Development (MySQL)
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/turtleman
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=password
export JPA_DDL_AUTO=update
```

#### Production (PostgreSQL)
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/turtleman
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=secure_password
export JPA_DDL_AUTO=validate
```

### 배포 전략

#### Docker 배포
```dockerfile
FROM eclipse-temurin:17-jre
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t turtleman-town .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://... \
  turtleman-town
```

#### Cloud 배포 (Render/Heroku)
- 자동으로 `PORT` 환경변수 감지
- PostgreSQL 자동 연결 (`DATABASE_URL`)
- Git push 기반 자동 배포

---

## 디자인 패턴 및 Best Practices

### 1. Dependency Injection
- **Constructor Injection** 사용 (`@RequiredArgsConstructor`)
- `@Autowired` 필드 주입 지양

### 2. Layered Architecture
- **Controller → Service → Repository** 계층 분리
- 각 레이어는 하위 레이어에만 의존

### 3. DTO 패턴 (향후 적용 예정)
- Entity를 직접 노출하지 않고 DTO 변환
- API 응답 구조 표준화

### 4. Exception Handling
- `@ControllerAdvice`를 통한 전역 예외 처리 (향후)
- 비즈니스 로직 예외 커스터마이징

### 5. Transaction Management
- `@Transactional` 어노테이션 활용
- Read-Only 최적화

---

## 향후 개선 사항

### 단기 (1-2개월)
- [ ] REST API 엔드포인트 추가
- [ ] DTO 레이어 도입
- [ ] 전역 예외 처리 (@ControllerAdvice)
- [ ] 입력 유효성 검증 (Validation)
- [ ] 계산 히스토리 저장 기능
- [ ] 포트폴리오 관리 기능

### 중기 (3-6개월)
- [ ] JWT 토큰 기반 인증 (API용)
- [ ] Redis 캐싱 적용
- [ ] 실시간 주가 연동 (API)
- [ ] 사용자 대시보드 커스터마이징
- [ ] 알림 기능 (이메일/푸시)

### 장기 (6개월+)
- [ ] 마이크로서비스 아키텍처 전환
- [ ] React/Vue 프론트엔드 분리
- [ ] 모바일 앱 개발
- [ ] AI 기반 투자 분석
- [ ] 소셜 로그인 (OAuth2)

---

## 라이센스 및 문의

- **프로젝트**: Turtleman Town (터틀맨 타운)
- **버전**: 0.0.1-SNAPSHOT
- **작성일**: 2025-10-17

이 문서는 개발자가 프로젝트 구조를 빠르게 이해하고 개발에 참여할 수 있도록 작성되었습니다.
