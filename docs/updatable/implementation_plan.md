# Spring Security & JWT 인증/인가 백엔드 서버 구현 계획서 (업데이트)

본 계획서는 필수 요구사항(Spring Security + JWT Access Token 기반 회원가입, 로그인, 내 정보 조회, Swagger API 문서화, 401/403 예외 처리)과 우대 요구사항(Spring Validation을 이용한 입력값 검증, RDB 기반 간결한 Refresh Token 처리, Logback 기반 프로필별 로깅 환경 구축, Docker & GitHub Actions CI/CD 파이프라인 수립)을 모두 포괄한 종합 기술 설계 및 구현 단계 정의서입니다.

## User Review Required

> [!IMPORTANT]
> **핵심 아키텍처 및 보안, CI/CD 설계 포인트**
> 
> * **회원 엔티티 정보**: `email`, `password`, `nickname` 필수 필드만으로 간결하게 구성합니다.
> * **Refresh Token 쿠키 전달**: 보안성(XSS 방지)을 높이기 위해 Refresh Token은 `HttpOnly` 쿠키에 설정해 반환합니다. Access Token은 일반 JSON 응답 바디에 실어 반환합니다.
> * **Refresh Token DB 저장**: H2/MySQL의 `refresh_token` 테이블에 저장 및 매핑하여 관리합니다.
> * **로깅 인프라**: `logback-spring.xml`을 활용해 Local(가독성 위주 콘솔 로그)과 Prod(Rolling File Appender 파일 로그 백업) 환경을 분리하여 적용합니다.
> * **배포 인프라 및 운영 환경**: **Oracle Cloud Free Tier** VM 환경을 선택하여, 단일 VM 서버 내부에 **Docker Compose**를 기반으로 Spring Boot 어플리케이션과 MySQL DB 컨테이너를 함께 구동합니다.
> * **Docker & GitHub Actions CI/CD**: OpenJDK 25 환경에 맞춘 멀티 스테이지 빌드 `Dockerfile`과 빌드-이미징-Oracle Cloud 서버 SSH 원격 배포 자동화 워크플로우를 생성합니다.

---

## Proposed Changes

### 1. Common & Logging Component (예외 처리 및 로깅)

#### [NEW] [logback-spring.xml](file:///home/perso/projects/2026_14th_final_study_BE/src/main/resources/logback-spring.xml)
프로필에 따라 콘솔 출력 또는 일별 파일 저장(Rolling File Appender)을 수행하는 Logback 구성 파일입니다.
* Local 프로필: 콘솔 컬러 로그 출력 (`DEBUG`/`INFO` 레벨)
* Prod 프로필: 콘솔 출력 외에 `/logs/app.log` 경로에 일별로 압축/롤링 보관 (최대 30일 보관, 파일 크기 10MB당 분할)

#### [NEW] [ErrorResponse.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/common/exception/ErrorResponse.java)
401, 403을 포함한 시스템 전반의 에러를 균일한 형태로 반환하는 DTO 객체입니다.

#### [NEW] [GlobalExceptionHandler.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/common/exception/GlobalExceptionHandler.java)
예외를 로깅(`log.error`)하고 `ErrorResponse` 형태로 감싸 반환하는 컨트롤러 어드바이스입니다.

---

### 2. Domain & Repository Component (엔티티 및 DB 레이어)

#### [NEW] [Role.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/domain/Role.java)
* `ROLE_USER`, `ROLE_ADMIN` 등 권한을 정의하는 Enum 클래스입니다.

#### [NEW] [User.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/domain/User.java)
* `id` (PK), `email` (로그인 ID 겸용, unique), `password` (암호화), `nickname`, `role` (Enum)을 필드로 갖는 JPA 엔티티입니다.

#### [NEW] [RefreshToken.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/domain/RefreshToken.java)
* `id` (PK), `email` (사용자 식별용 이메일, Unique), `token` (토큰 값), `expiryDate` (만료 일시)를 가지는 Refresh Token 데이터베이스 테이블용 엔티티입니다.

#### [NEW] [UserRepository.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/repository/UserRepository.java)
#### [NEW] [RefreshTokenRepository.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/repository/RefreshTokenRepository.java)

---

### 3. Security & JWT Component (인프라 및 필터 보안 레이어)

#### [NEW] [JwtProvider.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/security/JwtProvider.java)
* 최신 `jjwt 0.12.6` 스펙에 준수한 JWT 발급 및 파싱/검증 엔진을 구현합니다.

#### [NEW] [JwtAuthenticationFilter.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/security/JwtAuthenticationFilter.java)
* Authorization 헤더에서 Access Token을 파싱하여 Security Context에 인증 정보를 주입하는 보안 필터입니다.

#### [NEW] [CustomAuthenticationEntryPoint.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/security/CustomAuthenticationEntryPoint.java)
* 인증되지 않은 접근 시 발생하는 401 오류 처리를 전담하고, 에러 상황을 `log.warn`으로 상세히 남깁니다.

#### [NEW] [CustomAccessDeniedHandler.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/security/CustomAccessDeniedHandler.java)
* 인가 실패 시 발생하는 403 오류 처리를 전담하고, `log.warn`을 통해 침입 시도 또는 권한 미달 상황을 로깅합니다.

#### [NEW] [SecurityConfig.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/security/SecurityConfig.java)
* BCrypt 패스워드 인코더, CORS/CSRF Stateless 구성, 인증 예외 핸들러 연동, URL 접근 권한 등을 정의합니다.

---

### 4. DTO & Service & Controller Component (비즈니스 및 로깅 연동)

#### [NEW] DTO 클래스들 (`com.example.demo.dto`)
* `@Valid`를 활용한 이메일 형식 검증 및 패스워드 최소 길이 검증 애노테이션 설정

#### [NEW] [AuthService.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/service/AuthService.java)
* 회원가입, 로그인, 재발급 핵심 비즈니스 로직. 성공 및 실패 시 로그(`log.info`, `log.error`)를 정밀하게 남겨 추적성을 확보합니다.

#### [NEW] [UserService.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/service/UserService.java)
#### [NEW] [AuthController.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/controller/AuthController.java)
* Swagger UI와의 긴밀한 애노테이션 바인딩 적용. 로그인 성공 시 HTTP 쿠키에 `HttpOnly; Secure` 속성의 Refresh Token 탑재

#### [NEW] [UserController.java](file:///home/perso/projects/2026_14th_final_study_BE/src/main/java/com/example/demo/controller/UserController.java)

---

### 5. Docker & CI/CD Component (도커 및 자동화 파이프라인 - Oracle Cloud)

#### [NEW] [Dockerfile](file:///home/perso/projects/2026_14th_final_study_BE/Dockerfile)
Java 25 환경에 맞추어 `eclipse-temurin:25-jdk-alpine`을 사용해 빌드하고 `eclipse-temurin:25-jre-alpine`을 런타임으로 사용해 경량화된 컨테이너 이미지를 생성하는 멀티 스테이지 빌드 Dockerfile입니다.

#### [NEW] [docker-compose.yml](file:///home/perso/projects/2026_14th_final_study_BE/docker-compose.yml)
Oracle Cloud VM 상에서 DB(MySQL) 서비스 및 애플리케이션 컨테이너를 영속성 볼륨과 함께 기동하기 위한 도커 컴포즈 파일입니다.

#### [NEW] [deploy.yml](file:///home/perso/projects/2026_14th_final_study_BE/.github/workflows/deploy.yml)
GitHub Actions CI/CD 워크플로우 설정 파일입니다.
* 개인 포크 레포의 **개인 작업 브랜치(`week8/**`)** 변경 사항 감지 시 트리거
* Gradle 캐시 적용으로 빌드 성능 향상
* JUnit 테스트 자동 수행 및 빌드 (`./gradlew build`)
* Docker Buildx를 사용해 이미지 빌딩 후 GitHub Packages(GHCR)에 태그별 푸시
* 개인 레포 Repository Secrets를 기반으로 Oracle Cloud VM 서버로 SSH 연결 후 원격에서 최신 이미지 풀링 및 무중단 배포 트리거 템플릿 포함 (중앙 레포 main 변경과 격리되어 안전하게 테스트 가능)

---

## Verification Plan

### Automated Tests
- `./gradlew test`를 통해 예외 핸들링, JWT 토큰 발급/검증 유효성, 회원 가입 및 패스워드 인코더 테스트 코드를 자동으로 통과하는지 확인합니다.

### Manual Verification
1. **서버 구동 및 로깅 작동 확인**:
   * `./gradlew bootRun` 실행 후 콘솔 로그가 포맷팅되어 이쁘게 출력되는지 확인.
   * `logback-spring.xml`에서 의도한 로그 레벨 제어 및 로그 백업 파일의 존재 여부 확인.
2. **Swagger UI 확인**: `http://localhost:8080/swagger-ui.html`에 접속하여 API 명세 및 401/403 상세 예외 명세를 점검합니다.
3. **인증 예외 시나리오**:
   * 잘못된 토큰 혹은 빈 헤더로 마이페이지(`/api/v1/users/me`)를 두드렸을 때 예외 상황이 공통 `ErrorResponse` 포맷으로 예쁘게 파싱되어 오는지 확인하고, 로그 상에 `WARN` 레벨로 기록되는지 검증합니다.
4. **CI/CD 및 Docker 작동**:
   * 로컬에서 `docker build -t final-app .` 명령이 오류 없이 빌드 완료되는지 검증합니다.
