# 백엔드 구현 작업 진행 관리 상황판 (`task.md`)

- **최종 업데이트**: 2026-05-23
- **참여자**: Antigravity, USER

---

## 🚦 전체 진행 상황 요약
- **Phase 1 (공통 예외 및 기본 뼈대 구축)**: `[x]` 완료
- **Phase 2 (도메인 모델 및 리포지토리 구현)**: `[x]` 완료
- **Phase 3 (Security 및 JWT 인프라스트럭처 설정)**: `[x]` 완료
- **Phase 4 (핵심 서비스 및 컨트롤러 구현)**: `[x]` 완료
- **Phase 5 (Dockerizing 및 CI/CD 구축)**: `[ ]` 대기
- **Phase 6 (API 문서화 및 검증)**: `[ ]` 대기

---

## 📋 세부 진행 상황

### 📌 [Phase 1] 공통 예외 및 기본 뼈대 구축 `[x]`
- [x] 공통 API 에러 응답 객체 (`ErrorResponse`) 설계
- [x] 인증 실패 및 권한 부족 커스텀 예외 핸들러 구현 (`CustomAuthenticationEntryPoint`, `CustomAccessDeniedHandler`)
- [x] 예외 처리를 전역에서 잡아줄 `GlobalExceptionHandler` 구성
- [x] 프로필별(Local/Prod) 로깅 설정을 위한 `logback-spring.xml` 설계 및 로깅 공통 클래스 생성

### 📌 [Phase 2] 도메인 모델 및 리포지토리 구현 `[x]`
- [x] 회원 엔티티 (`User`) 및 권한 Enum (`Role`) 설계
- [x] 회원 리포지토리 (`UserRepository`) 구현
- [x] Refresh Token 엔티티 (`RefreshToken`) 및 리포지토리 (`RefreshTokenRepository`) 구현

### 📌 [Phase 3] Security 및 JWT 인프라스트럭처 설정 `[x]`
- [x] JWT 토큰 생성 및 검증을 담당할 `JwtProvider` 구현 (jjwt 0.12.6 빌더 패턴 사용)
- [x] JWT 필터 (`JwtAuthenticationFilter`) 구현 및 Security Filter Chain에 통합
- [x] Spring Security 설정 클래스 (`SecurityConfig`) 구성 (BCryptPasswordEncoder 빈 등록 및 경로별 접근 권한 설정)

### 📌 [Phase 4] 핵심 서비스 및 컨트롤러 구현 (비즈니스 로직 & 로깅 연동) `[x]`
- [x] 회원가입 및 로그인 서비스 로직 구현 (비밀번호 암호화 및 Access/Refresh Token 발급, 주요 단계 로깅 처리)
- [x] 토큰 재발급 서비스 구현 (`/api/v1/auth/reissue` - Refresh Token 쿠키 검증 및 새로운 Access Token 발급, 로깅 처리)
- [x] 내 정보 조회(마이페이지) 기능 구현 (`/api/v1/users/me`)
- [x] Spring Validation을 적용한 Request DTO 검증 추가 및 로그 연동

### 📌 [Phase 5] Dockerizing 및 CI/CD 구축 `[ ]`
- [ ] 멀티스테이지 빌드를 적용한 최적화된 경량 `Dockerfile` 작성
- [ ] DB(MySQL) 및 스프링 서버 컨테이너를 가동할 `docker-compose.yml` 작성
- [ ] `.github/workflows/deploy.yml` 파일 작성을 통한 빌드-테스트-이미징-Oracle Cloud 서버 SSH 원격 배포 자동화 구현

### 📌 [Phase 6] API 문서화 및 검증 `[ ]`
- [ ] Swagger (Springdoc OpenAPI) 애노테이션을 적용하여 API 명세 고도화
- [ ] 로컬 환경(H2 DB)에서 전체 인증/인가 시나리오 수동 테스트 및 검증
- [ ] 단위 테스트 코드 작성 (우대사항)
