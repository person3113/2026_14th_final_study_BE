# 백엔드 요구사항 구현 현황 상세 보고

## 📋 필수 요구사항 (Mandatory)

| 요구사항 항목 | 구현 여부 | 관련 주요 파일 및 구현 내용 |
|---|---|---|
| 인증/인가 (Security + JWT Access) | ✅ 완료 (100%) | `SecurityConfig.java`: Stateless 세션 정책 적용 및 JWT 필터 연동. `JwtAuthenticationFilter.java`: Request 헤더에서 `Bearer` 토큰을 파싱하고 검증 후 Context에 인증 객체 저장 |
| 회원가입 | ✅ 완료 (100%) | `AuthController.java` 및 `AuthService.java`: 이메일 중복 검사(`existsByEmail`) 및 `BCryptPasswordEncoder` 비밀번호 단방향 암호화 적용 |
| 로그인 | ✅ 완료 (100%) | `AuthController.java`: 로그인 성공 시 Access Token은 JSON Body로 반환하고, Refresh Token은 브라우저 탈취 예방을 위해 `HttpOnly`, `Secure`, `SameSite=None` 설정이 적용된 쿠키에 탑재하여 반환 |
| 내 정보 조회 (마이페이지) | ✅ 완료 (100%) | `UserController.java`: 인가된 요청에 한해 `@AuthenticationPrincipal`을 활용하여 Context에 저장된 사용자의 프로필(`email`, `nickname` 등)을 반환 |
| Swagger UI 활용 | ✅ 완료 (100%) | `application.yml`: `/swagger-ui.html` 경로 및 자동 정렬 옵션 활성화. 각 컨트롤러마다 `@Tag`, `@Operation`, `@ApiResponses` 어노테이션이 선언되어 있으며, `SecurityConfig.java`의 `@SecurityScheme` 설정을 통해 Swagger UI 상에서 Bearer JWT 토큰 주입 및 테스트 지원 |
| 서버 및 DB 배포 | ✅ 완료 (100%) | `Dockerfile`: JDK 25 경량 런타임 환경으로 패키징하는 최적화된 Multi-Stage 빌드 완료. `docker-compose.yml`: MySQL 8.0(UTF-8mb4 한글 지원) DB 컨테이너와 스프링 부트 애플리케이션 컨테이너를 함께 구동할 수 있도록 환경 구축 |
| 예외 처리 (401 / 403) | ✅ 완료 (100%) | `CustomAuthenticationEntryPoint.java`: 인증 만료/실패 시 `401 Unauthorized` 예외 응답을 규격화된 JSON 포맷으로 반환. `CustomAccessDeniedHandler.java`: 접근 권한 부족 시 `403 Forbidden` 예외 응답을 JSON 포맷으로 핸들링 |
| 합의된 API 명세서 작성 | ✅ 완료 (100%) | 코드 내 Swagger 문서 자동화가 완비되어 있고, `PULL_REQUEST_TEMPLATE.md`에 API 명세 및 트러블슈팅, 배포 URL 링크를 기록하여 팀원들과 공유할 수 있는 통합 PR 템플릿 구축 |

***

## ⭐ 우대 요구사항 (Preferred / Optional)

| 요구사항 항목 | 구현 여부 | 관련 주요 파일 및 구현 내용 |
|---|---|---|
| 요청 값 검증 (Spring Validation) | ✅ 완료 (100%) | `SignupRequest.java` & `LoginRequest.java`: `@NotBlank`, `@Email`, 최소 비밀번호 4자 지정을 위한 `@Size(min = 4)` 적용. `GlobalExceptionHandler.java`: Validation 에러 메시지를 가공하여 `400 BAD_REQUEST` 응답으로 가로채는 `RestControllerAdvice` 예외 핸들링 구현 |
| Refresh Token 인증/인가 | ✅ 완료 (100%) | 로그인 시 7일 유효한 Refresh Token을 발급해 DB(`RefreshTokenRepository`)에 저장(1인 1토큰 매핑). 재발급 요청 시 토큰 대조 후, 만료되거나 위변조된 토큰이 감지되면 즉시 DB에서 파기(`delete`) 및 재로그인 요구 안전장치 구현 완료 |
| GitHub Actions + Docker 기반 CI/CD 파이프라인 | ✅ 완료 (100%) | `deploy.yml`: `week8/**` 브랜치 푸시 시 자동 활성화되는 완전 자동화 파이프라인. **CI 단계**: JDK 25 빌드 및 Gradle 테스트 전체 자동 검증. **CD 단계**: GHCR에 Docker 이미지 빌드 및 푸시 후, SSH를 통해 Oracle Cloud VM에 자동 접속하여 `.env` 환경변수를 로드하고 `docker compose` 무중단 재배포 및 레거시 이미지 자동 정리(prune) |
| Logging | ✅ 완료 (100%) | 모든 컨트롤러, 서비스, 예외 처리 핸들러에 Lombok `@Slf4j` 적용하여 핵심 로직 흐름 및 경고/에러 로그 기록. `logback-spring.xml`: 일별 로그 파일 분할 보관 및 최대 30일 자동 아카이빙 설정 완료 |
| 테스트 코드 | ✅ 완료 (100%) | `JwtProviderTest.java`: JWT 발급, 만료/위변조 토큰 판독 등 세부 검증 단위 테스트 통과. `AuthServiceTest.java`: 중복 회원 예외 발생 여부, 패스워드 불일치 검증 등 서비스 로직 비즈니스 통합 테스트 탑재 완료 |

***

## 💡 추가 참고 사항

### 데이터베이스 연결 전략

- **로컬 환경** (`local`): 간편한 구동을 위해 H2 메모리 DB로 설정
- **빌드/운영 환경** (`prod`): Oracle Cloud 및 Docker Compose 내 `final-db` 컨테이너(MySQL 8.0)의 환경변수(`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`)를 자동 적용

### 추가 액션 아이템

현재 백엔드는 추가 구현 사항 없이 완성된 상태입니다. 이후 작업은 아래 두 가지만 확인하면 배포까지 정상 진행됩니다.

- **CORS 처리**: 프론트엔드 코드와 연동 시 발생하는 CORS 설정 확인
- **환경변수 입력**: 로컬 및 배포 서버에 프로파일별 환경변수 정상 입력 여부 확인