#  팀번호 - 이름(FE), 이름(BE)
: 팀 7 - 심성진(FE), 정성오(BE)
---

## 🛠 무엇을 어떻게 구현했나요?

### 필수 요구사항

#### 1️. 인증/인가 (Spring Security + JWT Access Token)

| 파일 | 역할                                                                                                           |
|------|--------------------------------------------------------------------------------------------------------------|
| `SecurityConfig.java` | BCrypt 패스워드 인코더 빈 등록, CORS/CSRF Stateless 구성, URL별 접근 권한 및 예외 핸들러 연동                                         |
| `JwtProvider.java` | jjwt로 Access/Refresh Token 발급, 파싱, 검증 엔진                                                                     |
| `JwtAuthenticationFilter.java` | Authorization 헤더에서 Access Token을 추출해 SecurityContext에 인증 정보를 주입하는 보안 필터                                      |
| `AuthController.java` | 회원가입(`POST /api/v1/auth/signup`), 로그인(`POST /api/v1/auth/login`), 재발급(`POST /api/v1/auth/reissue`) API 엔드포인트 |
| `AuthService.java` | 회원가입(이메일 중복 검사, 비밀번호 암호화), 로그인(비밀번호 검증, 토큰 발급), 재발급 핵심 비즈니스 로직                                               |
| `UserController.java` | 내 정보 조회(`GET /api/v1/users/me`) - SecurityContext의 인증 정보를 활용해 마이페이지 반환                                       |
| `UserService.java` | 이메일 기반 사용자 정보 조회 후 `UserResponse` DTO로 변환 반환                                                                 |

#### 2️. Swagger UI

| 파일 | 역할 |
|------|------|
| `AuthController.java`, `UserController.java` | Springdoc OpenAPI 애노테이션 적용으로 Swagger UI에 API 명세 자동 문서화 |

→ **Swagger UI**: [http://3.39.231.178:8080/swagger-ui.html](http://3.39.231.178:8080/swagger-ui.html)

#### 3. 백엔드 서버 및 DB 배포 (AWS)

| 파일 | 역할 |
|------|------|
| `Dockerfile` | `eclipse-temurin:25-jdk-alpine` (빌드) → `eclipse-temurin:25-jre-alpine` (런타임) 멀티스테이지 경량 빌드. prod 프로필로 실행 |
| `docker-compose.yml` | AWS EC2 위에서 MySQL DB 컨테이너와 스프링 부트 앱 컨테이너를 볼륨 영속성과 함께 단일 환경으로 구동 |

#### 4️. 예외 처리 (`401` / `403`)

| 파일 | 역할 |
|------|------|
| `ErrorResponse.java` | `status`, `error`, `message` 필드를 가진 공통 에러 응답 DTO (전체 예외 상황 균일 포맷) |
| `GlobalExceptionHandler.java` | `@RestControllerAdvice` 기반 전역 예외 처리기. Validation 오류, 비즈니스 예외, 500 서버 오류를 `ErrorResponse` 형태로 래핑 반환 |
| `CustomAuthenticationEntryPoint.java` | 인증되지 않은 접근 시 `401 Unauthorized`를 `ErrorResponse` JSON으로 반환, `log.warn` 로깅 |
| `CustomAccessDeniedHandler.java` | 인가 실패 시 `403 Forbidden`을 `ErrorResponse` JSON으로 반환, `log.warn` 로깅 |

#### 5️. API 명세서 (Swagger)

→ Swagger UI 링크: [http://3.39.231.178:8080/swagger-ui.html](http://3.39.231.178:8080/swagger-ui.html)

---

### 우대 요구사항 (선택)

#### 1️. 요청 값 검증 (Spring Validation)

| 파일 | 역할 |
|------|------|
| `SignupRequest.java` | `@Email`, `@NotBlank`, `@Size` 등 애노테이션으로 이메일 형식·비밀번호 최소 길이·닉네임 필수 입력 검증 |
| `LoginRequest.java` | 이메일·비밀번호 필수 입력 검증 |
| `GlobalExceptionHandler.java` | `MethodArgumentNotValidException` 처리 - 필드 바인딩 오류 메시지를 취합해 `ErrorResponse`로 반환 |

#### 2️. Refresh Token 활용 인증/인가

| 파일 | 역할 |
|------|------|
| `RefreshToken.java` | `email`, `token`, `expiryDate` 필드를 가진 Refresh Token DB 엔티티 |
| `RefreshTokenRepository.java` | Refresh Token CRUD JPA 리포지토리 |
| `AuthService.java` | 로그인 시 Refresh Token을 DB에 저장/갱신, 재발급 시 DB 검증 후 신규 Access Token 발급 |
| `AuthController.java` | 로그인 응답 시 Refresh Token을 `HttpOnly` + `Secure` 쿠키에 탑재, 재발급 엔드포인트에서 쿠키 추출 처리 |

#### 3️. GitHub Actions + Docker 기반 CI/CD 파이프라인

| 파일 | 역할 |
|------|------|
| `Dockerfile` | 멀티스테이지 빌드로 경량 JRE 이미지 생성 |
| `docker-compose.yml` | MySQL + 스프링 부트 컨테이너 통합 구성 (볼륨 영속성, 환경 변수 주입) |
| `.github/workflows/deploy.yml` | `week8/**` 브랜치 푸시 시 트리거 → Gradle 빌드 + 테스트 → Docker 이미지 빌드 후 GHCR 푸시 → AWS EC2 SSH 원격 배포 자동화 |

#### 4️. Logging

| 파일 | 역할 |
|------|------|
| `logback-spring.xml` | Local 프로필: ANSI 컬러 콘솔 출력(DEBUG). Prod 프로필: 콘솔 + 일별 Rolling File Appender(`/logs/app-yyyy-MM-dd.log`, 30일 보관, 10MB 분할) |
| `AuthService.java`, `UserService.java` 등 | `@Slf4j` 적용, 요청 진입·성공·비즈니스 예외·보안 예외 상황을 `log.info` / `log.warn` / `log.error` 레벨로 구조화 기록 |

#### 5️⃣ 테스트 코드

| 테스트 파일 | 주요 검증 시나리오 |
|---|---|
| `JwtProviderTest.java` | Access Token 발급·클레임 파싱 성공 / Refresh Token 발급·검증 성공 / 깨진 토큰 검증 실패 |
| `AuthServiceTest.java` | 회원가입 성공 / 중복 이메일 가입 시 예외 / 로그인 성공(토큰 2개 반환) / 잘못된 비밀번호 로그인 시 예외 |

---

## 🤔 왜 이렇게 설계/구현했나요?

### 필수 요구사항

- **401 / 403 분리**: Spring Security의 `AuthenticationEntryPoint`(인증 미완료 → 401)와 `AccessDeniedHandler`(인증은 됐지만 권한 부족 → 403)를 각각 별도 클래스로 구현하여, 두 에러를 개념적으로 명확히 분리했습니다. 두 핸들러 모두 `ErrorResponse` 공통 포맷으로 응답을 통일해 프론트엔드가 에러를 일관되게 처리할 수 있도록 했습니다.
- **Stateless 세션**: JWT 기반 인증이므로 `SessionCreationPolicy.STATELESS`를 적용해 서버에 세션 상태를 남기지 않도록 설계했습니다.

### 우대 요구사항 (선택)

- **Refresh Token 저장소로 RDB 선택**: Redis 등 별도 인프라를 추가하지 않고 기존 MySQL DB에 `refresh_token` 테이블을 두어 관리했습니다. 추가 인프라 비용 없이 만료 일시 기반 검증과 재사용 방지가 가능해 현재 규모에서 충분하다고 판단했습니다.
- **Refresh Token 전달을 HttpOnly 쿠키로**: 응답 바디에 담으면 JavaScript로 접근 가능해 XSS 공격에 취약합니다. `HttpOnly` + `Secure` 쿠키에 담으면 브라우저가 JS 접근을 차단해 탈취 위험을 낮출 수 있어 이 방식을 택했습니다.
- **CI/CD 트리거를 개인 작업 브랜치(`week8/**`)로 한정**: 중앙 레포의 `main` 브랜치와 격리해, 팀 전체에 영향을 주지 않고 개인 포크 레포에서 독립적으로 배포 테스트를 진행할 수 있도록 설계했습니다.

---

## 🤝 협업 기록

### API 명세

→ Swagger UI: [http://3.39.231.178:8080/swagger-ui.html](http://3.39.231.178:8080/swagger-ui.html)

---

## 💭 고민했던 점 / 아직 모르겠는 점 / 어려웠던 점

**Spring Security + JWT 컴포넌트 간 흐름이 머릿속에 잘 안 들어왔습니다.**

`JwtProvider`, `JwtAuthenticationFilter`, `SecurityConfig`, `CustomAuthenticationEntryPoint`, `CustomAccessDeniedHandler` 각각의 역할은 이해했는데, 실제 요청이 들어왔을 때 이것들이 어느 순서로 맞물려 동작하는지 전체 그림이 바로 그려지지 않았습니다. 특히 필터가 SecurityContext에 인증 정보를 주입하는 시점과, 예외 핸들러가 개입하는 시점의 구분이 헷갈렸습니다.

**Logback 설정이 실제로 의도대로 작동하는지 확신이 없었습니다.**

`logback-spring.xml`을 작성하고 프로필별(`local` / `prod`) Appender를 분리하는 구조는 구현했는데, prod 환경에서 Rolling File Appender가 정말 파일로 쌓이는지, 로그 레벨 제어가 설정한 대로 되는지 직접 검증해보지 못한 채로 넘어간 부분이 애매하게 남아 있습니다.

---

## 🔥 트러블슈팅 (선택)

### Issue 1 — `DB_USERNAME=root` 설정으로 MySQL 컨테이너 기동 실패

**문제**: GitHub Secrets에 `DB_USERNAME=root`로 설정했더니 `final-db` 컨테이너가 계속 재시작되며 뜨지 않았습니다. MySQL 8.0은 `MYSQL_USER` 환경변수로 `root` 계정을 생성하는 것을 명시적으로 금지합니다. (`root`는 `MYSQL_ROOT_PASSWORD`로만 관리)  
**해결**: GitHub Secrets의 `DB_USERNAME` 값을 `root` 이외의 이름(예: `user`)으로 변경 후, `docker compose down -v`로 기존 깨진 볼륨을 제거하고 재배포했습니다.

### Issue 2 — DB 준비 전에 앱이 접속 시도해 Spring Boot 기동 실패

**문제**: `depends_on: db` 설정만으로는 MySQL 컨테이너가 시작됨만 기다릴 뿐, MySQL 서버가 완전히 초기화되어 쿼리를 받을 준비가 됐는지는 보장하지 않습니다. 앱 컨테이너가 DB 연결을 시도하는 시점에 MySQL이 아직 초기화 중이어서 `UnknownHostException` / 커넥션 거절이 발생했습니다.  
**해결**: `docker-compose.yml`의 db 서비스에 `healthcheck`를 추가하고, app 서비스의 `depends_on`을 `condition: service_healthy`로 변경해 MySQL이 실제로 준비된 후에만 앱이 기동되도록 수정했습니다.

### Issue 3 — t3.micro OOM으로 MySQL 컨테이너 반복 종료

**문제**: AWS EC2 t3.micro(RAM 1GB)에 MySQL 8.0과 Spring Boot JVM이 함께 뜨면 메모리 합계가 1GB를 초과해, Linux OOM Killer가 MySQL 프로세스를 강제 종료하고 `restart: always` 정책에 의해 무한 재시작되는 현상이 발생했습니다.  
**해결**:
  - MySQL에 `--innodb-buffer-pool-size=64M`, `--performance-schema=OFF`, `--max-connections=50` 옵션을 추가해 메모리 사용량을 낮췄습니다.
  - `JAVA_TOOL_OPTIONS: "-Xmx256m -Xms128m"` 환경변수로 JVM 힙 상한을 제한했습니다.
  - EC2 서버에 2GB Swap 파일을 추가해 물리 메모리 부족 시 스왑 공간을 활용할 수 있도록 했습니다.

---

## 🖥️ 배포 URL

<!-- 과제를 구현한 배포 URL을 프론트엔드, 백엔드 각각 첨부해주세요 -->

### 프론트엔드 URL
[바로가기]()

### 백엔드 URL
[바로가기](http://3.39.231.178:8080/swagger-ui.html)