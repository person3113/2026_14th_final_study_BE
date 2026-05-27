# [Phase 6] API 문서화 및 검증 작업 워크플로우

- **작성일시**: 2026-05-24 (일) 01:16
- **작성자**: Antigravity & USER
- **작업 브랜치**: `week8/#팀번호-이름`

---

## 📌 Phase 6 목표
개발 완료된 인증/인가 API들을 Swagger UI 상에 완벽하게 시각화 및 명세화하고, 실제 데이터베이스 연결(H2) 상에서 전체 시나리오를 수동 점검합니다. 나아가 핵심 비즈니스 로직에 대해 JUnit 테스트 코드를 작성해 무결성을 확실하게 보증합니다.

---

## 📋 세부 작업 TODO 리스트 및 워크플로우

### [Task 1] Swagger (Springdoc) API 문서화 고도화
- [x] **컨트롤러 애노테이션 매핑**:
  - `AuthController` 및 `UserController`에 `@Tag`, `@Operation`, `@ApiResponse` 설정 적용
  - 회원가입 시 성공(`200`), 유효성 검증 실패(`400`), 이메일 중복(`400`) 등의 상황을 명세에 노출
  - 로그인 시 Access Token 바디 반환 및 Refresh Token 보안 쿠키 발급 명세 기재
  - 마이페이지 조회 시 토큰 없이 접근 시 `401 Unauthorized` 포맷이 정상적으로 노출됨을 예외 스펙에 명시
- [x] **Swagger JWT 인증 필터 적용**:
  - Swagger UI에서 바로 `Authorize` 자물쇠 버튼을 눌러 Bearer 토큰을 설정하고 테스트할 수 있도록 `SecurityConfig`에 `OpenAPI` 빈 설정 또는 `@SecurityScheme` 연동 추가

### [Task 2] 로컬 H2 환경에서 전체 통합 시나리오 테스트
- [x] **서버 기동 및 Swagger UI 접속**:
  - `./gradlew bootRun` 기동 후 `http://localhost:8080/swagger-ui.html` 정상 진입 확인
- [x] **시나리오 1: 회원가입 및 DTO 유효성 검사**:
  - 잘못된 이메일 형식이나 누락된 비밀번호 전달 시 `400 Bad Request`와 함께 적절한 한국어 에러 피드백이 오는지 점검
  - 정상 정보로 가입 성공 확인
- [x] **시나리오 2: 로그인 및 토큰 수령**:
  - 로그인 성공 시 Access Token 수령 여부 및 브라우저 Response Header에 `Set-Cookie`로 `HttpOnly` 속성의 `refreshToken`이 명확히 담기는지 점검
- [x] **시나리오 3: 내 정보 조회 (인증 테스트)**:
  - Authorization 헤더 없이 마이페이지 호출 시 `401 Unauthorized`와 공통 ErrorResponseJSON이 오는지 점검
  - 수령한 Access Token을 헤더(`Authorization: Bearer <Access_Token>`)에 얹어 요청 시 200 OK와 함께 이메일, 닉네임이 정상 반환되는지 점검
- [x] **시나리오 4: 토큰 재발급**:
  - 쿠키에 담긴 `refreshToken`을 바탕으로 `/api/v1/auth/reissue`를 요청했을 때 정상적으로 신규 Access Token이 발행되어 수령되는지 검증

### [Task 3] JUnit 5 단위 테스트 코드 작성
- [x] **JWT 유틸 테스트 구현**:
  - `JwtProviderTest` 클래스를 작성하여 임의의 토큰 생성 후 올바르게 파싱되는지, 만료 여부가 정상 판정되는지 검사
- [x] **서비스 레이어 테스트 구현**:
  - `AuthServiceTest` 클래스를 작성해 회원가입 성공, 가입 이메일 중복 예외 발생, 로그인 성공 시 패스워드 대조 로직 및 토큰 발급 동작 확인 단위 테스트 구동

---

## 🛠️ 예상 패키지 및 파일 구조 요약

```
src/
├── main/java/com/example/demo/
│   └── security/
│       └── SecurityConfig.java (Swagger SecurityScheme 애노테이션 연동)
└── test/java/com/example/demo/
    ├── security/
    │   └── JwtProviderTest.java
    └── service/
        └── AuthServiceTest.java
```

---

## 🔍 작업 검증 및 완료 조건 (Verification)

1. **테스트 코드 올그린(All Green) 통과**:
   - 로컬에서 `./gradlew test`를 수행해 구현한 단위 테스트 코드가 단 하나의 실패 없이 깨끗하게 성공하는지 확인
2. **Swagger UI 명세 확인**:
   - 명세 페이지에 접속하여 Auth와 User 관련 엔드포인트 명세가 아름답게 시각화되어 있는 것을 눈으로 최종 검토
