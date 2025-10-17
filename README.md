# 🐢 터틀맨 타운 (Turtleman Town)

> 주식 평균 매수 단가 계산 및 투자 관리를 위한 Spring Boot 웹 애플리케이션

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 목차

- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [시작하기](#시작하기)
- [프로젝트 구조](#프로젝트-구조)
- [API 엔드포인트](#api-엔드포인트)
- [배포](#배포)
- [문서](#문서)

---

## 🎯 주요 기능

- **🔐 사용자 인증 및 권한 관리**
  - Spring Security 기반 폼 로그인
  - 역할 기반 접근 제어 (RBAC: Admin/User)
  - BCrypt 비밀번호 암호화

- **📊 물타기 계산기**
  - 평균 매수 단가 계산
  - 목표 평단가 역산 (추가 매수량 계산)
  - 실시간 계산 결과 제공

- **📱 반응형 웹 UI**
  - Thymeleaf 템플릿 엔진
  - 모바일/데스크톱 최적화
  - 황금 거북이 테마 🐢

- **🗄️ 다중 데이터베이스 지원**
  - Local: H2 (In-Memory)
  - Dev: MySQL
  - Prod: PostgreSQL

---

## 🛠 기술 스택

### Backend
- **Spring Boot** 3.3.5
- **Spring Security** 6
- **Spring Data JPA**
- **Thymeleaf**
- **Lombok**

### Database
- H2 Database (Local)
- MySQL 8+ (Development)
- PostgreSQL 14+ (Production)

### Build Tool
- Maven 3.8+
- Java 17

---

## 🚀 시작하기

### 필수 요구사항
- **JDK 17** 이상
- **Maven 3.8+**
- (선택) Docker

### 로컬 실행

#### 1. 프로젝트 클론
```bash
git clone https://github.com/wonderful1003/java-web-proto.git
cd java-web-proto
```

#### 2. 빌드 및 실행
```bash
# Maven으로 빌드 및 실행
mvn spring-boot:run

# 또는 JAR 파일 생성 후 실행
mvn clean package -DskipTests
java -jar target/java-web-proto-0.0.1-SNAPSHOT.jar
```

#### 3. 애플리케이션 접속
- **메인 페이지**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비어있음)

#### 4. 로그인 정보
```
관리자 계정: admin / admin123
일반 사용자: user / user123
```

---

## 📁 프로젝트 구조

```
java-web-proto/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/          # 설정 클래스 (Security, DataLoader)
│   │   │   ├── controller/      # 컨트롤러 (Auth, Dashboard, Web)
│   │   │   ├── entity/          # JPA 엔티티 (User, Role, Menu)
│   │   │   ├── repository/      # Repository 인터페이스
│   │   │   ├── service/         # 비즈니스 로직
│   │   │   ├── security/        # 인증 처리
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── static/          # 정적 리소스 (CSS, JS, HTML)
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── index.html
│   │       ├── templates/       # Thymeleaf 템플릿
│   │       │   ├── login.html
│   │       │   └── dashboard.html
│   │       └── application.properties
│   └── test/
├── ARCHITECTURE.md              # 아키텍처 설계서 📖
├── README.md
└── pom.xml
```

자세한 구조는 [ARCHITECTURE.md](ARCHITECTURE.md) 참고

---

## 🌐 API 엔드포인트

### 인증
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/login` | 로그인 페이지 | ❌ |
| POST | `/perform-login` | 로그인 처리 | ❌ |
| POST | `/logout` | 로그아웃 | ✅ |

### 페이지
| Method | Endpoint | Description | Auth | Role |
|--------|----------|-------------|------|------|
| GET | `/` | 메인 페이지 | ❌ | - |
| GET | `/dashboard` | 대시보드 | ✅ | USER, ADMIN |
| GET | `/calculator` | 계산기 | ✅ | USER, ADMIN |

### 관리자 (예정)
| Method | Endpoint | Description | Auth | Role |
|--------|----------|-------------|------|------|
| GET | `/admin/users` | 회원 관리 | ✅ | ADMIN |
| GET | `/admin/roles` | 권한 관리 | ✅ | ADMIN |

---

## 🐳 배포

### Docker로 실행
```bash
# 이미지 빌드
docker build -t turtleman-town .

# 컨테이너 실행
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/db \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  turtleman-town
```

### Cloud Run 배포
```bash
gcloud init
gcloud config set project <YOUR_PROJECT_ID>
gcloud services enable run.googleapis.com artifactregistry.googleapis.com cloudbuild.googleapis.com

REGION=asia-northeast3   # Seoul
REPO=demo-repo
gcloud artifacts repositories create $REPO --repository-format=docker --location=$REGION

IMAGE="$REGION-docker.pkg.dev/<YOUR_PROJECT_ID>/$REPO/java-web-proto:1.0.0"
gcloud builds submit --tag "$IMAGE"
gcloud run deploy turtleman-town \
  --image "$IMAGE" \
  --region $REGION \
  --allow-unauthenticated \
  --memory 512Mi
```

### 환경 변수 설정
```bash
# PostgreSQL 연결 (Production)
export SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/turtleman
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=secure_password
export JPA_DDL_AUTO=validate

# MySQL 연결 (Development)
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/turtleman
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=password
export JPA_DDL_AUTO=update
```

---

## 📖 문서

- **[ARCHITECTURE.md](ARCHITECTURE.md)** - 상세 아키텍처 설계서
  - 시스템 아키텍처
  - 패키지 구조
  - 데이터베이스 설계 (ERD)
  - 보안 설계
  - 개발 가이드

---

## 🔧 개발 환경 설정

### IDE 설정 (IntelliJ IDEA)
1. **Import Project**: Maven 프로젝트로 가져오기
2. **Lombok Plugin**: Lombok 플러그인 설치 및 Annotation Processing 활성화
3. **Run Configuration**: Spring Boot Application 설정

### 코드 스타일
- **Java**: Google Java Style Guide
- **Indentation**: 4 spaces
- **Line Length**: 120 characters

---

## 🚧 향후 계획

### v0.2.0 (예정)
- [ ] REST API 엔드포인트
- [ ] 계산 히스토리 저장
- [ ] 포트폴리오 관리
- [ ] DTO 레이어 도입

### v0.3.0 (예정)
- [ ] JWT 인증
- [ ] 실시간 주가 연동
- [ ] 사용자 설정 기능
- [ ] 알림 기능

---

## 📄 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.

---

## 👨‍💻 개발자

**프로젝트 관리자**: [wonderful1003](https://github.com/wonderful1003)

---

## 🙏 기여

버그 리포트, 기능 제안, Pull Request는 언제나 환영합니다!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

<div align="center">

**🐢 투자는 느리고 신중하게, 거북이처럼! 🐢**

Made with ❤️ by Turtleman Town Team

</div>
