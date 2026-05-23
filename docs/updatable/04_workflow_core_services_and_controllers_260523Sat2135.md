# [Phase 4] 핵심 서비스 및 컨트롤러 구현 작업 워크플로우

- **작성일시**: 2026-05-23 (토) 21:35
- **작성자**: Antigravity & USER
- **작업 브랜치**: `week8/#팀번호-이름`

---

## 📌 Phase 4 목표
회원가입, 로그인, 마이페이지(내 정보 조회), 그리고 RDB 및 HttpOnly 쿠키 기반의 리프레시 토큰 재발급 비즈니스 로직을 구축합니다. 또한 Spring Validation을 활용해 안전한 입력 값 검증을 적용하고 각 주요 진입점에 로깅을 세밀하게 적용합니다.

---

## 📋 세부 작업 TODO 리스트 및 워크플로우

### [Task 1] 회원가입, 로그인, 재발급 DTO 클래스 설계
- [ ] **패키지 생성**: `com.example.demo.dto`
- [ ] **SignupRequest 구현**:
  - 이메일: `@Email(message = "올바른 이메일 형식이어야 합니다.") @NotBlank(message = "이메일은 필수 입력값입니다.") String email`
  - 비밀번호: `@Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.") @NotBlank(message = "비밀번호는 필수 입력값입니다.") String password`
  - 닉네임: `@NotBlank(message = "닉네임은 필수 입력값입니다.") String nickname`
- [ ] **LoginRequest 구현**:
  - `email`, `password` 필드 및 검증 애노테이션
- [ ] **TokenResponse 구현**:
  - `String accessToken`만 제공 (Refresh Token은 보안 쿠키로 전달)
- [ ] **UserResponse 구현**:
  - 마이페이지 조회용 `Long id`, `String email`, `String nickname` 필드 정의

### [Task 2] 회원가입, 로그인, 재발급 핵심 서비스 (`AuthService`, `UserService`) 구현
- [ ] **패키지 생성**: `com.example.demo.service`
- [ ] **AuthService 구현**:
  - **회원가입 (`signup`)**:
    - 이메일 중복 검사 (`UserRepository.existsByEmail` 활용) 및 예외 처리
    - `PasswordEncoder.encode`를 통해 패스워드를 암호화하여 저장
    - 주요 단계 로깅 (`log.info("Successfully registered user: {}", email)`)
  - **로그인 (`login`)**:
    - 이메일로 사용자 조회 및 존재 여부 검사
    - `PasswordEncoder.matches`를 활용하여 비밀번호 매칭 검사
    - `JwtProvider`를 통한 Access Token 및 Refresh Token 동시 생성
    - 발급된 Refresh Token 정보를 `RefreshToken` 엔티티로 데이터베이스에 저장/업데이트
  - **토큰 재발급 (`reissue`)**:
    - 요청에 담긴 Refresh Token 유효성 검증
    - 데이터베이스의 Refresh Token 조회 및 비교 분석
    - 검증 통과 시 신규 Access Token 발급 및 반환
- [ ] **UserService 구현**:
  - **내 정보 조회 (`getUserProfile`)**:
    - 이메일을 기반으로 사용자 정보 조회 후 `UserResponse` 변환 반환

### [Task 3] 컨트롤러 구현 및 HttpOnly 보안 쿠키 연동 (`AuthController`, `UserController`)
- [ ] **패키지 생성**: `com.example.demo.controller`
- [ ] **AuthController 구현**:
  - 회원가입 API: `POST /api/v1/auth/signup` (`@Valid` 필수)
  - 로그인 API: `POST /api/v1/auth/login`
    - 로그인 성공 시, Refresh Token은 **`ResponseCookie`** 또는 **`Cookie`** API를 사용하여 **`HttpOnly`**, **`Secure`** (실제 HTTPS 상에서 동작), **`Path=/`** 설정을 적용해 응답 헤더(`Set-Cookie`)에 적재
    - Access Token은 JSON 응답 바디로 반환
  - 토큰 재발급 API: `POST /api/v1/auth/reissue`
    - HttpServletRequest의 Cookie 배열에서 리프레시 토큰 추출 후 `AuthService` 위임 처리
- [ ] **UserController 구현**:
  - 내 정보 조회 API: `GET /api/v1/users/me`
    - SecurityContextHolder에서 파싱된 인증 정보 객체(`Principal` 또는 `@AuthenticationPrincipal`)를 활용해 이메일을 획득하고, `UserService`를 연동하여 내 정보 반환

---

## 🛠️ 예상 패키지 및 파일 구조 요약

```
src/main/java/com/example/demo/
├── dto/
│   ├── SignupRequest.java
│   ├── LoginRequest.java
│   ├── TokenResponse.java
│   └── UserResponse.java
├── service/
│   ├── AuthService.java
│   └── UserService.java
└── controller/
    ├── AuthController.java
    └── UserController.java
```

---

## 🔍 작업 검증 및 완료 조건 (Verification)

1. **Gradle 빌드 점검**:
   - 구현 후 `./gradlew compileJava` 시 문법 오류 없이 완벽 빌드되는지 검증
2. **로컬 API 기능 테스트 (H2 DB)**:
   - H2 데이터베이스 및 로컬에서 Postman/Swagger를 활용하여 회원가입 -> 로그인(쿠키 및 바디 정상 발급) -> 마이페이지 조회 -> 토큰 재발급 과정이 한 오차도 없이 통과하는지 시나리오 점검
3. **단위 테스트 코드 작성**:
   - `AuthService` 및 `UserService` 비즈니스 로직에 대해 JUnit 테스트 코드를 작성하여 무결성 점검
