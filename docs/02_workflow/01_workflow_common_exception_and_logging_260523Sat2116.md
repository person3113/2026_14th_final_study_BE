# [Phase 1] 공통 예외 및 기본 뼈대 구축 작업 워크플로우

- **작성일시**: 2026-05-23 (토) 21:16
- **작성자**: Antigravity & USER
- **작업 브랜치**: `week8/#팀번호-이름`

---

## 📌 Phase 1 목표
Spring Security와 JWT 연동 과정에서 발생할 수 있는 401(인증 실패), 403(권한 부족) 에러와 비즈니스/검증 예외를 일관된 JSON 데이터 포맷으로 프론트엔드에 응답할 수 있는 공통 예외 처리 체계를 구축합니다. 또한 Local과 Production 서버 로깅 프로필을 분리하여 안정적인 시스템 모니터링 환경을 조성합니다.

---

## 📋 세부 작업 TODO 리스트 및 워크플로우

### [Task 1] 공통 API 에러 응답 객체 (`ErrorResponse`) 설계
- [x] **패키지 생성**: `com.example.demo.common.exception`
- [x] **ErrorResponse 클래스 구현**: 
  - 응답에 포함될 필수 필드(`status`, `error`, `message`) 정의
  - Lombok `@Getter`, `@Builder` 애노테이션 적용하여 불변 객체로 설계
  - 다양한 에러 상황에서 손쉽게 생성할 수 있는 팩토리 메서드 혹은 빌더 흐름 검증

### [Task 2] 인증 실패 및 권한 부족 커스텀 예외 핸들러 구현
- [x] **패키지 생성**: `com.example.demo.security.exception` (또는 `com.example.demo.security` 하위)
- [x] **CustomAuthenticationEntryPoint 구현 (401 Unauthorized)**:
  - `AuthenticationEntryPoint` 인터페이스 구현
  - HTTP 요청 실패 정보를 `log.warn`을 통해 추적 가능한 로그로 기록
  - HTTP 응답 헤더를 `application/json;charset=UTF-8`로 셋업하고 `ErrorResponse`를 ObjectMapper를 통해 JSON 응답 본문으로 쓰기 구현
- [x] **CustomAccessDeniedHandler 구현 (403 Forbidden)**:
  - `AccessDeniedHandler` 인터페이스 구현
  - 비인가 사용자의 접근 시도 및 요청 URL을 `log.error` 또는 `log.warn`으로 상세 로깅
  - `ErrorResponse`를 활용하여 일관된 403 JSON 에러 결과 반환 구현

### [Task 3] 전역 예외 처리기 (`GlobalExceptionHandler`) 구성
- [x] **클래스 생성**: `com.example.demo.common.exception.GlobalExceptionHandler`
- [x] **기본 애노테이션 설정**: `@RestControllerAdvice` 적용
- [x] **Spring Validation 예외 처리 핸들러 구현**:
  - `@Valid` 검증 오류 시 발생하는 `MethodArgumentNotValidException` 처리
  - 필드 바인딩 에러 메시지들을 가독성 있게 취합하여 `ErrorResponse`에 반환 및 `log.warn` 로깅
- [x] **일반 비즈니스/기타 예외 처리 핸들러 구현**:
  - `IllegalArgumentException`, `IllegalStateException` 등 비즈니스 제약 위반 예외 처리
  - 정의되지 않은 시스템 예외(`Exception.class`) 발생 시 `500 Internal Server Error` 공통 포맷으로 마스킹 후 `log.error`로 전체 StackTrace 로깅

### [Task 4] 프로필별 로깅 설정 (`logback-spring.xml`) 설계
- [x] **설정 파일 생성**: `src/main/resources/logback-spring.xml`
- [x] **Local 프로필 Appender 및 로깅 정책 구성**:
  - `<springProfile name="local">` 지정
  - 콘솔 출력을 담당하는 `ConsoleAppender` 및 ANSI 가독성 컬러 패턴 적용
  - 전체 애플리케이션 로그 레벨 `INFO`, 개발 도메인 패키지(`com.example.demo`) 로그 레벨 `DEBUG` 설정
- [x] **Prod 프로필 Appender 및 로깅 정책 구성**:
  - `<springProfile name="prod">` 지정
  - 콘솔 출력 외에 파일로 저장 및 관리하는 `RollingFileAppender` 정의
  - 일별 롤링 패턴 지정 (`/logs/app-%d{yyyy-MM-dd}.log`) 및 최대 30일 보관, 10MB 기준 분할 압축 설정
- [x] **로깅 공통 적용성 검증**:
  - Spring Boot 구동 시 지정한 프로필(`local`/`prod`)에 따른 Appender 및 로그 레벨이 오차 없이 활성화되는지 부팅 로그 확인

---

## 🛠️ 예상 패키지 및 파일 구조 요약

```
src/main/
├── java/com/example/demo/
│   ├── common/
│   │   └── exception/
│   │       ├── ErrorResponse.java
│   │       └── GlobalExceptionHandler.java
│   └── security/
│       ├── handler/
│       │   ├── CustomAccessDeniedHandler.java
│       │   └── CustomAuthenticationEntryPoint.java
│       └── SecurityConfig.java (Phase 3에서 예외 핸들러 연동 예정)
└── resources/
    ├── application.yml
    └── logback-spring.xml
```

---

## 🔍 작업 검증 및 완료 조건 (Verification)

1. **Gradle 빌드 무중단 점검**:
   - `01` 단계 구현 후 `./gradlew compileJava` 및 `./gradlew build -x test` 시 컴파일 오류 없이 정상 빌드 완료 여부 검증
2. **콘솔 로그 확인**:
   - 로컬 구동 시 지정된 ANSI 로깅 컬러 패턴이 깨짐 없이 콘솔창에 올바르게 가독성 높은 텍스트로 찍히는지 점검
3. **단위 테스트 작성**:
   - `GlobalExceptionHandler`와 `ErrorResponse`가 예외 상황 시 정상적으로 원하는 포맷의 JSON 바디 데이터를 내뱉는지 MockMvc 단위 테스트를 통해 기능 무결성 1차 검증
