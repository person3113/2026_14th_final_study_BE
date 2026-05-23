# 백엔드 개발 구현 방향성 및 기술 의의 (의논 및 결정 사항)

- **최종 업데이트**: 2026-05-23
- **참여자**: Antigravity (AI 개발 파트너), USER (백엔드 개발자)

---

## 1. 현재 리포지토리 상태 분석 및 빌드 검증

### 🛠️ 개발 환경 및 빌드 설정 (`build.gradle`)
* **Java 버전**: 25 (최신 사양 적용)
* **Spring Boot 버전**: 4.0.6 (현재 선언된 스펙 준수)
* **빌드 결과**: `./gradlew tasks` 검증 결과 빌드 정상 작동 확인 완료. 주어진 버전 설정을 그대로 유지하고 개발을 진행합니다.

---

## 2. 최종 합의 및 결정 사항 (2026-05-23)

### 🙋‍♂️ 회원(User) 엔티티 스펙
* **결정**: 간결함을 유지하기 위해 추가 필드 없이 필수 필드만 구현합니다.
* **필드 구성**:
  * `Long id` (PK, Auto Increment)
  * `String email` (로그인 ID 겸용, unique, 중복 불가)
  * `String password` (암호화된 비밀번호)
  * `String nickname` (사용자 닉네임)
  * `Role role` (권한 등급, 기본값: `USER`)

### 🔑 실무형 Refresh Token 설계 (간결함과 실무 표준의 조화)
* **저장 방식**: RDB(H2/MySQL)에 `RefreshToken` 엔티티/테이블을 단독으로 설계하여 저장합니다. Redis 등 별도의 인프라를 추가하지 않고 데이터베이스를 활용해 간결하게 관리합니다.
  * **테이블 필드**: `id`, `user_email`(또는 `user_id`), `token_value`, `expiry_date`
* **전달 방식**:
  * **Access Token**: 로그인 성공 시 응답 Body(JSON)로 반환. (프론트엔드에서 Authorization 헤더로 전송)
  * **Refresh Token**: 보안을 위해 **`HttpOnly` 및 `Secure` 설정이 적용된 쿠키(Cookie)**로 반환. 
    * *장점*: 프론트엔드에서 자바스크립트로 접근할 수 없어 XSS 공격에 안전하며, 실무에서 널리 쓰이는 표준 방식입니다.
  * **재발급 흐름**: 프론트엔드에서 `/api/v1/auth/reissue`를 요청할 때 쿠키에 담긴 Refresh Token을 검증하여 새로운 Access Token을 발급합니다.

### 📝 로깅 전략 (Logging) - 우대사항 추가
* **기술 스택**: Spring Boot 기본 로깅 프레임워크인 SLF4J + Logback 사용
* **Local 설정**: 콘솔에 가독성 높은 ANSI 컬러 포맷으로 실시간 출력 (`INFO` 레벨 기본, 애플리케이션 핵심 패키지는 `DEBUG` 활성화 가능).
* **Prod 설정**: 콘솔 출력과 동시에 파일 시스템에 일별 백업되는 Rolling File Appender 구성 (`/logs/app-%d{yyyy-MM-dd}.log` 형식, 최대 30일 보관 및 파일당 10MB 기준 분할).
* **구현 방식**: 컨트롤러 및 주요 서비스 클래스에 `@Slf4j`를 적용하여 요청 진입점, 성공 여부, 비즈니스 예외 및 보안 관련 예외 상황을 체계적으로 구조화하여 기록합니다.

### 🚀 CI/CD 파이프라인 (GitHub Actions + Docker) - 우대사항 추가
* **Dockerizing**: Java 25 환경에 맞추어 `eclipse-temurin:25-jre-alpine` 베이스 이미지 기반의 경량 멀티스테이지 빌드 `Dockerfile` 구성.
* **GitHub Actions Workflow** (`.github/workflows/deploy.yml`):
  * **빌드 단계**: 개인 포크 레포의 **개인 작업 브랜치(`week8/**`)**로의 푸시 발생 시 트리거. JDK 25 셋업 후 `./gradlew bootJar` 수행.
  * **이미징 단계**: Docker CLI를 활용하여 빌드 아티팩트를 이미지화한 뒤 GitHub Packages(GHCR) 또는 Docker Hub에 자동 푸시.
  * **배포 단계**: 개인 포크 레포의 Secrets에 안전하게 저장된 Oracle Cloud 가상 서버 정보를 기반으로 원격 SSH 접속 후 `docker compose pull` 및 `docker compose up -d` 명령어를 통해 무중단 배포 스크립트 실행 구조 마련. 중앙 레포 머지 전 개별 독립 테스트 가능.

---

## 3. 배포 및 운영 환경 결정 사항 (2026-05-23 추가)

* **배포 인프라 (Vendor)**: **Oracle Cloud Free Tier** 가상 서버(VM) 활용
* **인프라 구축 방식**: 
  - 가상 서버 내부에 **Docker 및 Docker Compose** 엔진 설치
  - **스프링 부트 어플리케이션 컨테이너**와 **MySQL DB 컨테이너**를 단일 인프라 환경 내에서 유기적으로 구성 (볼륨 바인딩을 통해 DB 데이터 영속성 보장)
  - 추가 유료 인프라 비용 부담 없이 평생 무료 자원 내에서 완벽한 실무형 배포 환경을 지향합니다.
* **CI/CD 연동**: GitHub Actions 워크플로우를 활용하여 빌드 및 도커 이미징 후, SSH 프로토콜로 Oracle Cloud 서버에 원격 명령을 송신해 최신 컨테이너를 가동하도록 연동합니다.

---

## 4. 전체 구현 로드맵 (큰 그림 TODO 리스트)

### 📌 [Phase 1] 공통 예외 및 기본 뼈대 구축
- [ ] 공통 API 에러 응답 객체 (`ErrorResponse`) 설계
- [ ] 인증 실패 및 권한 부족 커스텀 예외 핸들러 구현 (`CustomAuthenticationEntryPoint`, `CustomAccessDeniedHandler`)
- [ ] 예외 처리를 전역에서 잡아줄 `GlobalExceptionHandler` 구성
- [ ] 프로필별(Local/Prod) 로깅 설정을 위한 `logback-spring.xml` 설계 및 로깅 공통 클래스 생성

### 📌 [Phase 2] 도메인 모델 및 리포지토리 구현
- [ ] 회원 엔티티 (`User`) 및 권한 Enum (`Role`) 설계
- [ ] 회원 리포지토리 (`UserRepository`) 구현
- [ ] Refresh Token 엔티티 (`RefreshToken`) 및 리포지토리 (`RefreshTokenRepository`) 구현

### 📌 [Phase 3] Security 및 JWT 인프라스트럭처 설정
- [ ] JWT 토큰 생성 및 검증을 담당할 `JwtProvider` 구현 (jjwt 0.12.6 빌더 패턴 사용)
- [ ] JWT 필터 (`JwtAuthenticationFilter`) 구현 및 Security Filter Chain에 통합
- [ ] Spring Security 설정 클래스 (`SecurityConfig`) 구성 (BCryptPasswordEncoder 빈 등록 및 경로별 접근 권한 설정)

### 📌 [Phase 4] 핵심 서비스 및 컨트롤러 구현 (비즈니스 로직 & 로깅 연동)
- [ ] 회원가입 및 로그인 서비스 로직 구현 (비밀번호 암호화 및 Access/Refresh Token 발급, 주요 단계 로깅 처리)
- [ ] 토큰 재발급 서비스 구현 (`/api/v1/auth/reissue` - Refresh Token 쿠키 검증 및 새로운 Access Token 발급, 로깅 처리)
- [ ] 내 정보 조회(마이페이지) 기능 구현 (`/api/v1/users/me`)
- [ ] Spring Validation을 적용한 Request DTO 검증 추가 및 로그 연동

### 📌 [Phase 5] Dockerizing 및 CI/CD 구축 (Oracle Cloud 연동 고려)
- [ ] 멀티스테이지 빌드를 적용한 최적화된 경량 `Dockerfile` 작성
- [ ] Oracle Cloud 상에서 DB(MySQL) 및 스프링 서버 컨테이너를 가동할 `docker-compose.yml` 템일릿 작성
- [ ] `.github/workflows/deploy.yml` 파일 작성을 통한 빌드-테스트-이미징-Oracle Cloud 서버 SSH 원격 배포 자동화 구현

### 📌 [Phase 6] API 문서화 및 검증
- [ ] Swagger (Springdoc OpenAPI) 애노테이션을 적용하여 API 명세 고도화
- [ ] 로컬 환경(H2 DB)에서 전체 인증/인가 시나리오 수동 테스트 및 검증
- [ ] 단위 테스트 코드 작성 (우대사항)
