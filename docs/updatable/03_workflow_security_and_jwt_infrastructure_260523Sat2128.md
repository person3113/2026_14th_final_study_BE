# [Phase 3] Security 및 JWT 인프라스트럭처 설정 작업 워크플로우

- **작성일시**: 2026-05-23 (토) 21:28
- **작성자**: Antigravity & USER
- **작업 브랜치**: `week8/#팀번호-이름`

---

## 📌 Phase 3 목표
최신 `jjwt 0.12.6` 스펙에 부합하는 안전한 JWT 토큰 인프라를 구축하고, Spring Security의 Security Filter Chain에 커스텀 JWT 인증 필터를 통합하여 무상태(Stateless) 기반의 강력한 보안 접근 제어망을 완성합니다.

---

## 📋 세부 작업 TODO 리스트 및 워크플로우

### [Task 1] JWT 토큰 공급자 (`JwtProvider`) 구현
- [ ] **패키지 생성**: `com.example.demo.security` (또는 `com.example.demo.security.jwt`)
- [ ] **JwtProvider 클래스 구현**:
  - `@Component` 애노테이션 탑재
  - `application.yml`에 선언된 `jwt.secret-key` 주입 받아 서명 키(`SecretKey`) 생성 패턴 구현 (최신 `Keys.hmacShaKeyFor` 활용)
  - **Access Token 생성 메소드** 설계 (유효시간 30분, 페이로드에 email 및 role 적재)
  - **Refresh Token 생성 메소드** 설계 (유효시간 7일, 페이로드에 email 적재)
  - **토큰 검증 및 파싱 메소드**:
    - `Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)` 패턴을 이용하여 서명 위변조 및 만료 일시를 견고하게 검증
  - **토큰에서 Claim 추출 메소드**:
    - 토큰에서 사용자 이메일(`email`) 및 권한 리스트 추출

### [Task 2] JWT 인증 필터 (`JwtAuthenticationFilter`) 구현
- [ ] **JwtAuthenticationFilter 클래스 구현**:
  - `OncePerRequestFilter` 상속
  - HTTP 요청의 `Authorization` 헤더에서 Access Token 추출 로직 구현 (`Bearer ` 접두사 파싱)
  - 추출한 토큰이 유효한 경우, `JwtProvider`를 통해 이메일 및 권한을 파싱하여 Spring Security의 인증 객체(`UsernamePasswordAuthenticationToken`) 생성
  - 생성한 인증 객체를 `SecurityContextHolder.getContext().setAuthentication(auth)`에 적재하여 필터 체인 통과 중 인증 상태를 유지하도록 구성
  - 토큰이 만료되었거나 서명이 유효하지 않을 경우, SecurityContext를 깨끗하게 비우고 예외 로그 출력 후 체인 통과 처리 (이후 EntryPoint에서 401 처리 유도)

### [Task 3] Spring Security 설정 클래스 (`SecurityConfig`) 구성
- [ ] **SecurityConfig 클래스 구현**:
  - `@Configuration`, `@EnableWebSecurity` 애노테이션 설정
  - **패스워드 암호화 빈 등록**:
    - `@Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }`
  - **SecurityFilterChain 빈 등록 및 설정**:
    - CSRF(Cross-Site Request Forgery) 비활성화
    - 세션 정책을 `SessionCreationPolicy.STATELESS`로 전환하여 서버 무상태성 보장
    - URL 접근 권한 정의:
      - 회원가입(`POST /api/v1/auth/signup`), 로그인(`POST /api/v1/auth/login`), 토큰 재발급(`POST /api/v1/auth/reissue`) 경로는 전체 허용(`permitAll()`)
      - Swagger UI 관련 경로 (`/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`) 전체 허용(`permitAll()`)
      - 그 외의 모든 API 요청 경로는 인증 요구 (`authenticated()`)
    - **커스텀 예외 핸들러 연동**:
      - `CustomAuthenticationEntryPoint`(401) 및 `CustomAccessDeniedHandler`(403)를 `.exceptionHandling(...)` 설정에 등록
    - **커스텀 필터 삽입**:
      - `JwtAuthenticationFilter`를 `UsernamePasswordAuthenticationFilter` 직전(`addFilterBefore`)에 삽입하여 토큰 검증 필터링 선제 처리

---

## 🛠️ 예상 패키지 및 파일 구조 요약

```
src/main/java/com/example/demo/security/
├── SecurityConfig.java
├── JwtProvider.java
├── JwtAuthenticationFilter.java
└── handler/ (Phase 1에서 기작성)
    ├── CustomAccessDeniedHandler.java
    └── CustomAuthenticationEntryPoint.java
```

---

## 🔍 작업 검증 및 완료 조건 (Verification)

1. **Gradle 빌드 및 부트 부팅**:
   - 구현 후 `./gradlew compileJava` 시 오류가 없으며, Spring Boot 서버가 정상적으로 기동하는지 점검
2. **Security 필터 체인 설정 로그 검토**:
   - 서버 구동 로그 중 Spring Security가 로딩되면서 정의한 URL 패턴 및 Filter Chain 순서가 콘솔에 정상적으로 등록되어 출력되는지 검토
3. **단위 테스트 및 빌드 무결성 점검**:
   - `JwtProvider`를 주입받아 임의의 토큰 생성 후 올바르게 파싱 및 검증되는지 독립적인 JUnit 5 테스트 코드를 실행하여 검증
