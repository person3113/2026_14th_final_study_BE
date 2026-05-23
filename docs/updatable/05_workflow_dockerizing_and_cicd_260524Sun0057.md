# [Phase 5] Dockerizing 및 CI/CD 구축 작업 워크플로우

- **작성일시**: 2026-05-24 (일) 00:57
- **작성자**: Antigravity & USER
- **작업 브랜치**: `week8/#팀번호-이름`

---

## 📌 Phase 5 목표
회원 웹 서비스를 경량화된 Docker 이미지로 포장(Dockerizing)하고, 로컬 및 Oracle Cloud 상용 인프라에서 DB와 유기적으로 구동할 Docker Compose 설정을 확보합니다. 나아가 개인 작업 브랜치(`week8/**`)로의 푸시가 감지될 때 빌드, 이미징(GHCR), Oracle Cloud 가상 서버로의 SSH 원격 자동 무중단 배포를 관장하는 GitHub Actions 파이프라인을 최종 정초합니다.

---

## 📋 세부 작업 TODO 리스트 및 워크플로우

### [Task 1] 경량 멀티스테이지 빌드 `Dockerfile` 구성
- [x] **프로젝트 루트에 Dockerfile 생성**:
  - 베이스 빌드 이미지: `eclipse-temurin:25-jdk-alpine` 지정 (JDK 25 Alpine 경량 버전)
  - 빌드 단계:
    - 작업 디렉토리 `/app` 지정
    - Gradle Wrapper 및 `build.gradle` 등을 복사하여 의존성을 캐싱하고 소스를 아우르는 빌드 가동 (`./gradlew bootJar -x test --no-daemon`)
  - 실행 단계:
    - 베이스 실행 이미지: `eclipse-temurin:25-jre-alpine` 지정 (컴파일 도구를 뺀 초경량 JRE Alpine)
    - 빌드 단계의 Jar 복사 및 실행 포트(`8080`) 노출(`EXPOSE 8080`)
    - 실행 명령어: `ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]`

### [Task 2] 컨테이너 통합 가동용 `docker-compose.yml` 구축
- [x] **프로젝트 루트에 docker-compose.yml 생성**:
  - H2 외에 상용 운영 DB 연동을 위한 **MySQL DB 서비스** 정의
    - 이미지: `mysql:8.0` (또는 최신 stable 버전)
    - 컨테이너 및 서비스 이름: `db`
    - 환경 변수: MySQL root 비밀번호 및 사용할 DB명 지정 (`final` DB)
    - 포트: `3306:3306`
    - 데이터 보존을 위한 영속성 볼륨 지정: `mysql_data:/var/lib/mysql`
  - **스프링 부트 애플리케이션 서비스** 정의
    - 서비스 이름: `app`
    - 환경 변수 주입: `application.yml`이 요구하는 `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`, `${JWT_SECRET_KEY}`를 컨테이너 환경 변수로 안전하게 주입
    - H2 프로필이 아닌 MySQL DB 서버와 유기적으로 물리도록 접속 URL 주입: `jdbc:mysql://db:3306/final`
    - `depends_on: [db]` 설정을 부여하여 데이터베이스 가동 후 어플리케이션이 구동되도록 시퀀스 제어
    - 포트: `8080:8080`

### [Task 3] GitHub Actions 자동 무중단 배포 워크플로우 (`deploy.yml`) 작성
- [x] **파일 생성**: `.github/workflows/deploy.yml`
- [x] **워크플로우 트리거 조건 지정**:
  - `on: push: branches: [ "week8/**" ]` (개인 작업 브랜치 푸시 시 자동 배포)
- [x] **CI (빌드 및 검증) 단계**:
  - GitHub 가상 호스트 상에 JDK 25 셋업
  - Gradle 캐시 기동으로 빌드 효율성 향상
  - 자동 빌드 및 단위 테스트 (`./gradlew build`)
- [x] **Docker 이미징 및 GitHub Packages (GHCR) 푸시 단계**:
  - GitHub Actions의 `actions/checkout` 및 `docker/login-action`을 연동하여 GHCR 로그인
  - `docker/build-push-action`을 활용해 `Dockerfile`을 멀티스테이지로 빌딩 및 이미지 태그(`/final-app:latest`)를 GHCR에 업로드
- [x] **CD (Oracle Cloud SSH 배포) 단계**:
  - `appleboy/ssh-action` 플러그인 활용
  - 개인 포크 레포의 Secrets에 저장된 `SSH_HOST`, `SSH_USERNAME`, `SSH_KEY`를 꺼내 Oracle Cloud 가상 서버로 원격 SSH 접속
  - 원격 서버 내 작업 폴더로 이동하여 `docker compose down`, `docker compose pull`, `docker compose up -d` 명령어 기동으로 최신 버전 무중단 컨테이너 기동 수행 템플릿 포함

---

## 🛠️ 예상 패키지 및 파일 구조 요약

```
프로젝트 루트/
├── Dockerfile
├── docker-compose.yml
└── .github/
    └── workflows/
        └── deploy.yml
```

---

## 🔍 작업 검증 및 완료 조건 (Verification)

1. **로컬 도커 빌드 검증**:
   - 로컬 터미널에서 `docker build -t final-app .` 명령을 실행해 이미지 빌드가 에러 없이 가볍고 성공적으로 완수되는지 확인
2. **도커 컴포즈 가동 확인**:
   - `docker compose up -d`를 로컬에 기동하여 스프링과 MySQL 컨테이너가 아무 장애 없이 한 묶음으로 실행되는지 검증
3. **CI/CD 명세 검토**:
   - `.github/workflows/deploy.yml` 파일 내에 작성된 개인 레포용 Secrets 매핑과 배포 스크립트의 구문 무결성 확인
