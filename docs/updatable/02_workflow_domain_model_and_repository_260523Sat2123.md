# [Phase 2] 도메인 모델 및 리포지토리 구현 작업 워크플로우

- **작성일시**: 2026-05-23 (토) 21:23
- **작성자**: Antigravity & USER
- **작업 브랜치**: `week8/#팀번호-이름`

---

## 📌 Phase 2 목표
회원 엔티티(`User`), 회원의 역할 정보(`Role`), 그리고 로그인 연장 및 토큰 관리를 위한 데이터베이스 저장 기반의 리프레시 토큰 엔티티(`RefreshToken`) 및 리포지토리 레이어를 정밀하게 설계하고 구축합니다.

---

## 📋 세부 작업 TODO 리스트 및 워크플로우

### [Task 1] 회원 권한 Enum (`Role`) 및 회원 엔티티 (`User`) 설계
- [x] **패키지 생성**: `com.example.demo.domain`
- [x] **Role Enum 설계**:
  - `ROLE_USER`, `ROLE_ADMIN` 등 스프링 시큐리티 표준 접두사(`ROLE_`)를 만족하도록 정의
- [x] **User JPA 엔티티 구현**:
  - 테이블명: `users` 매핑
  - PK 필드: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id`
  - 이메일: `@Column(nullable = false, unique = true) String email`
  - 비밀번호: `@Column(nullable = false) String password` (암호화된 값 적재용)
  - 닉네임: `@Column(nullable = false) String nickname`
  - 역할: `@Enumerated(EnumType.STRING) @Column(nullable = false) Role role`
  - 가독성 높은 디버깅 로그를 위해 Lombok `@ToString` 적용 (비밀번호 필드는 `@ToString.Exclude` 처리하여 보안성 유지)

### [Task 2] 회원 리포지토리 (`UserRepository`) 구현
- [x] **패키지 생성**: `com.example.demo.repository`
- [x] **UserRepository 인터페이스 구현**:
  - `JpaRepository<User, Long>` 상속
  - 이메일을 통한 사용자 탐색 및 중복 체크 쿼리 메소드 설계:
    - `Optional<User> findByEmail(String email)`
    - `boolean existsByEmail(String email)`

### [Task 3] Refresh Token 엔티티 (`RefreshToken`) 및 리포지토리 구현
- [x] **RefreshToken JPA 엔티티 구현**:
  - 테이블명: `refresh_token` 매핑
  - PK 필드: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id`
  - 사용자 이메일: `@Column(nullable = false, unique = true) String email` (사용자당 단 하나의 활성 리프레시 토큰만 소유하도록 유니크 키 설정)
  - 토큰값: `@Column(nullable = false) String token`
  - 만료 일시: `@Column(nullable = false) LocalDateTime expiryDate`
  - 비즈니스 로직(토큰 값 업데이트, 만료 체크)을 엔티티 객체 내부에 응집하여 개발
- [x] **RefreshTokenRepository 인터페이스 구현**:
  - `JpaRepository<RefreshToken, Long>` 상속
  - 토큰 검색 및 만료 일시 기반 삭제 쿼리 설계:
    - `Optional<RefreshToken> findByToken(String token)`
    - `Optional<RefreshToken> findByEmail(String email)`
    - `void deleteByEmail(String email)`

---

## 🛠️ 예상 패키지 및 파일 구조 요약

```
src/main/java/com/example/demo/
├── domain/
│   ├── Role.java
│   ├── User.java
│   └── RefreshToken.java
└── repository/
    ├── UserRepository.java
    └── RefreshTokenRepository.java
```

---

## 🔍 작업 검증 및 완료 조건 (Verification)

1. **빌드 안정성 확인**:
   - `Phase 2` 구현 완료 후 `./gradlew compileJava` 수행하여 JPA 엔티티와 리포지토리 인터페이스 간 타입 미스매치나 빌드 오류가 없는지 검증
2. **JPA DDL 자동 생성 검증**:
   - 애플리케이션 임시 기동 시 `application.yml`의 `ddl-auto: create` 정책에 따라 H2 DB상에 `users` 및 `refresh_token` 테이블 스키마가 의도한 데이터 사양(Unique, Nullable 등)으로 정확히 자동 매핑 생성되는지 부트 실행 로그를 통해 검토
3. **간단한 영속성 데이터 테스트 코드 (선택)**:
   - `DataJpaTest` 또는 간단한 리포지토리 save/find 테스트 코드를 작성하여 DB 정상 조회 및 정합성을 확실하게 검증
