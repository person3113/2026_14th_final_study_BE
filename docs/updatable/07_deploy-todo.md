# 🚀 AWS EC2 + GitHub Actions CI/CD 전체 연동 TODO 가이드

> **목적**: 현재 완성된 코드베이스를 AWS EC2 서버에 배포하고, CI/CD 파이프라인이 정상 작동하는지 검증하는 전체 작업 흐름 정리  
> **작성일**: 2026-05-27  
> **기준 파일**: `.github/workflows/deploy.yml`, `Dockerfile`, `docker-compose.yml`  
> **변경 이력**: Oracle Cloud → **AWS Free Tier** 전환

---

## 📌 전제 조건 이해 (읽고 시작하기)

현재 구축된 CI/CD 파이프라인의 전체 흐름:

```
로컬에서 week8/** 브랜치 푸시
    → GitHub Actions 자동 트리거
    → [CI] Gradle 빌드 + 테스트 자동 수행
    → [CD] Docker 이미지 빌드 → GHCR(GitHub Packages) 업로드
    → [CD] AWS EC2에 SSH 접속
    → [CD] EC2 내에서 docker compose pull + docker compose up -d 실행
    → 서버 배포 완료 🎉
```

완성까지 필요한 단계: **PHASE 1~4**

> ⚠️ **AWS Free Tier 주의사항**: EC2 t2.micro 인스턴스는 월 750시간 무료. 1개 인스턴스 상시 가동 시 약 720시간으로 무료 한도 내에 들어옴. 단, **12개월 한정** 무료이며 이후 과금됨. 학습 목적이 끝나면 인스턴스를 중지(Stop)하거나 종료(Terminate)할 것.

---

## PHASE 1: AWS EC2 인스턴스 생성

### [ ] 1-1. AWS 계정 생성 및 로그인
- 접속: https://aws.amazon.com/ko/free/
- **AWS 계정 생성** 클릭 후 이메일, 비밀번호, 결제 수단 등록
  - 신용/체크카드 필요 (무료 한도 초과 시 과금 방지를 위해 등록 후 **Billing Alert 설정 권장**)
- 가입 후 콘솔(https://console.aws.amazon.com)에 로그인

> 💡 **과금 경보 설정 권장** (선택사항):  
> AWS 콘솔 → **Billing** → **Budgets** → **Create budget** → "Zero spend budget" 템플릿 선택 → 이메일 알림 설정  
> → 1달러라도 요금이 발생하면 이메일로 알림 수신

### [ ] 1-2. EC2 인스턴스 생성

AWS 콘솔 상단 검색창에서 **EC2** 검색 → **Instances** → **Launch instances**

**아래 설정으로 생성:**

| 항목 | 권장 설정 |
|---|---|
| **Name** | `final-key` (자유롭게 지정) |
| **AMI (이미지)** | `Ubuntu Server 22.04 LTS (HVM), SSD Volume Type` ← **Free tier eligible** 표시 확인 |
| **Instance type** | `t2.micro` ← **Free tier eligible** 표시 확인 |
| **Key pair** | ⭐ **"Create new key pair"** 클릭 → 이름 입력 → **RSA** + **.pem** 선택 → **Create** → `.pem` 파일 자동 다운로드 |
| **Storage** | 8 GiB gp3 (기본값, Free tier 30GB 한도 내) |
| **Network settings** | 아래 별도 설명 참조 |

**Network settings (보안 그룹 설정):**
- **"Create security group"** 선택
- **"Allow SSH traffic from"** 체크 → `My IP` 선택 (본인 IP만 SSH 허용)
- **"Allow HTTP traffic from the internet"** 체크 (선택)
- 나머지는 기본값 유지

→ **Launch instance** 클릭

### [ ] 1-3. 보안 그룹에 8080 포트 추가

EC2 인스턴스 생성 후 8080 포트를 추가로 열어야 합니다.

**AWS 콘솔** → **EC2** → **Instances** → 생성된 인스턴스 클릭 → 하단 **Security** 탭 → **Security groups** 링크 클릭 → **Edit inbound rules**

**아래 규칙 추가:**

| Type | Protocol | Port range | Source | 설명 |
|---|---|---|---|---|
| Custom TCP | TCP | **8080** | `0.0.0.0/0` | Spring Boot 앱 포트 |

→ **Save rules** 클릭

> ✅ **22번 SSH 포트**는 인스턴스 생성 시 이미 추가됨 (확인만 할 것)  
> ✅ AWS EC2는 Oracle Cloud와 달리 **내부 방화벽(iptables) 별도 설정이 불필요**. 보안 그룹 설정만으로 외부 접근 제어 완료.

### [ ] 1-4. Public IP(Elastic IP) 확인 및 기록

**AWS 콘솔** → **EC2** → **Instances** → 인스턴스 선택 → 상세 정보의 **Public IPv4 address** 복사

> ⚠️ **주의**: EC2 기본 Public IP는 **인스턴스 재시작 시 변경됨**.  
> 지속적으로 고정 IP가 필요하면 **Elastic IP** 할당 권장:  
> EC2 → **Elastic IPs** → **Allocate Elastic IP address** → 생성 후 인스턴스에 **Associate**

### [ ] 1-5. SSH 접속 테스트 (로컬 터미널에서)

다운로드된 `.pem` 파일을 사용해 EC2 접속:

```bash
# 다운로드한 .pem 파일 권한 설정 (필수 - 없으면 SSH 거부됨)
chmod 400 ~/Downloads/final-key.pem

# SSH 접속 테스트 (ubuntu: AWS Ubuntu AMI 기본 유저명)
ssh -i ~/Downloads/final-key.pem ubuntu@{EC2_PUBLIC_IP}
```

`ubuntu@ip-xxx-xxx-xxx-xxx:~$` 프롬프트가 보이면 접속 성공 → PHASE 2 진행

---

## PHASE 2: EC2 서버 내부 환경 세팅

> SSH로 EC2 내부에 접속한 상태에서 아래 명령어들을 순서대로 실행

### [ ] 2-1. 시스템 패키지 업데이트

```bash
sudo apt-get update && sudo apt-get upgrade -y
```

### [ ] 2-2. Docker 설치

```bash
# Docker 공식 설치 스크립트 사용
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 현재 사용자(ubuntu)를 docker 그룹에 추가 (sudo 없이 docker 실행 가능하게)
sudo usermod -aG docker ubuntu

# 그룹 변경 즉시 적용
newgrp docker

# Docker 설치 확인
docker --version
# 예상 출력: Docker version 26.x.x, build xxxxxxx
```

### [ ] 2-3. Docker Compose 설치

```bash
# Docker Compose V2 플러그인 방식 설치
sudo apt-get install -y docker-compose-plugin

# 설치 확인
docker compose version
# 예상 출력: Docker Compose version v2.x.x
```

### [ ] 2-4. 설치 완료 확인

```bash
# Docker 데몬 자동 시작 설정 (서버 재부팅 시에도 Docker 자동 구동)
sudo systemctl enable docker
sudo systemctl start docker

# 최종 확인
docker ps   # 에러 없이 빈 목록이 나오면 정상
```

---

## PHASE 3: GitHub Repository Secrets 등록

GitHub Actions의 `deploy.yml`이 참조하는 Secret 값들을 모두 등록합니다.

**경로**: 개인 포크 레포 → **Settings** → **Secrets and variables** → **Actions** → **"New repository secret"**

### [ ] 3-1. 등록해야 할 Secrets 목록 (6개)

| Secret 이름 | 등록할 값                              | 설명 |
|---|------------------------------------|---|
| `SSH_HOST` | EC2 Public IP (예: `52.78.xxx.xxx`) | PHASE 1-4에서 복사한 EC2 Public IPv4 주소 |
| `SSH_USERNAME` | `ubuntu`                           | AWS Ubuntu AMI 기본 접속 유저명 (고정값) |
| `SSH_KEY` | `.pem` 파일의 전체 텍스트 내용               | PHASE 1-2에서 다운로드한 `.pem` 파일 전체 내용 |
| `DB_USERNAME` | 원하는 MySQL 유저명 (예: `root`)          | MySQL 데이터베이스 접속 사용자명 |
| `DB_PASSWORD` | 비밀번호 (예: `1234`)                   | MySQL 데이터베이스 접속 비밀번호 |
| `JWT_SECRET_KEY` | 64자 이상 랜덤 문자열                      | JWT 토큰 서명 Secret Key |

---

> ⭐ **SSH_KEY 등록 방법 (가장 중요)**
>
> 로컬 터미널에서 `.pem` 파일 내용 전체 출력:
> ```bash
> cat ~/Downloads/final-key.pem
> ```
> 출력된 아래 전체 텍스트를 복사하여 `SSH_KEY` Secret 값으로 등록:
> ```
> -----BEGIN RSA PRIVATE KEY-----
> MIIEowIBAAKCAQEA...
> (중간 내용)
> ...xyzABCD==
> -----END RSA PRIVATE KEY-----
> ```
> ⚠️ 첫 줄(`-----BEGIN...`)부터 마지막 줄(`-----END...`)까지 **줄바꿈 포함 전체**를 복사해야 함

---

> ⭐ **JWT_SECRET_KEY 생성 방법**
>
> ```bash
> # 로컬 터미널에서 랜덤 64바이트 키 생성
> openssl rand -base64 64
> ```
> 출력된 문자열 전체를 복사하여 등록

### [ ] 3-2. GitHub Actions 쓰기 권한 확인

GHCR에 이미지를 Push하려면 레포 Actions 쓰기 권한이 필요합니다.

**개인 포크 레포** → **Settings** → **Actions** → **General** → **Workflow permissions** → **"Read and write permissions"** 선택 → **Save**

### [ ] 3-3. Secrets 등록 완료 확인

Settings → Secrets and variables → Actions 화면에서 **6개** 항목이 모두 목록에 나타나는지 확인  
(값은 보안상 마스킹 처리되어 보이지 않음 — 이름만 확인)

---

## PHASE 4: 첫 배포 트리거 및 검증

### [ ] 4-1. 현재 브랜치 확인

현재 `deploy.yml`의 트리거 설정:
```yaml
on:
  push:
    branches:
      - "week8/**"   # ← 이 패턴의 브랜치에 push해야 Actions 실행됨
```

로컬 브랜치 확인:
```bash
git branch
# week8/feat/auth, week8/main 등 week8/로 시작하는지 확인
```

`week8/`로 시작하지 않으면 브랜치 생성:
```bash
git checkout -b week8/main
```

### [ ] 4-2. 첫 배포 Push (Actions 트리거)

```bash
# 현재 변경사항 커밋 (변경사항 없으면 --allow-empty 옵션 사용)
git add .
git commit -m "chore: trigger first CI/CD pipeline on AWS EC2"

# 개인 포크 레포로 push → GitHub Actions 자동 시작
git push origin week8/main
```

### [ ] 4-3. GitHub Actions 실행 확인

**개인 포크 레포** → **Actions** 탭에서 실행 중인 워크플로우 확인

진행 단계:
1. **build** 잡: Gradle 빌드 + 테스트 검증 (약 3~5분)
2. **deploy** 잡: Docker 이미지 빌드 → GHCR 푸시 → SSH 원격 배포 (약 5~10분)

각 잡 이름 클릭 → 세부 로그 확인 가능

### [ ] 4-4. EC2 서버에서 컨테이너 동작 확인

로컬 터미널에서 EC2 SSH 접속:
```bash
ssh -i ~/Downloads/final-key.pem ubuntu@{EC2_PUBLIC_IP}
```

EC2 내부에서:
```bash
# 실행 중인 컨테이너 확인 (final-app, final-db 2개 모두 Up 상태여야 함)
docker ps

# Spring Boot 앱 실시간 로그 확인
docker logs final-app -f

# MySQL DB 로그 확인
docker logs final-db
```

### [ ] 4-5. 외부에서 최종 접속 확인

```bash
# 로컬 터미널에서 API 응답 확인 ({EC2_PUBLIC_IP}를 실제 IP로 교체)
curl http://{EC2_PUBLIC_IP}:8080/v3/api-docs
```

브라우저에서 Swagger UI 접속:
```
http://{EC2_PUBLIC_IP}:8080/swagger-ui.html
```

Swagger UI 화면이 정상 출력되면 **배포 완료** ✅

---

## 🔧 트러블슈팅 체크리스트

### GitHub Actions 빌드(build) 단계 실패

- [ ] **로컬에서 먼저 빌드 확인**: `./gradlew build` 로컬 성공 여부 확인
- [ ] **테스트 프로파일 확인**: `src/test/resources/application-test.yml` 파일 존재 여부 (없으면 테스트 실행 시 H2 연결 설정 없어 실패)
- [ ] **Java 버전 확인**: `deploy.yml`의 `java-version: '25'`와 로컬 `build.gradle`의 `sourceCompatibility` 일치 여부

### Docker 이미지 GHCR Push 실패

- [ ] **Actions 쓰기 권한 확인**: Settings → Actions → General → **"Read and write permissions"** 활성화 여부 (PHASE 3-2 참고)
- [ ] **레포 가시성 확인**: Private 레포라면 GHCR도 Private이 됨. Packages 설정에서 공개 여부 확인

### SSH 원격 접속 실패

- [ ] `SSH_HOST`: EC2 **Public** IPv4 IP인지 확인 (Private IP 넣으면 접속 불가)
- [ ] `SSH_USERNAME`: 반드시 `ubuntu` (AWS Ubuntu AMI 고정값)
- [ ] `SSH_KEY`: `.pem` 파일 내용을 **BEGIN부터 END까지 전체** 복사했는지 확인
- [ ] EC2 **보안 그룹 Inbound Rules**에 22번 TCP 포트가 열려있는지 확인
- [ ] Elastic IP를 연결했다면 해당 Elastic IP를 `SSH_HOST`에 등록했는지 확인

### 컨테이너는 뜨는데 8080 포트 접속 불가

- [ ] **AWS 보안 그룹** Inbound Rules에 8080 TCP 규칙 추가됐는지 확인 (PHASE 1-3 참고)
  - AWS EC2는 내부 ufw/iptables 건드릴 필요 없음. 보안 그룹만 확인
- [ ] `docker logs final-app` 에서 스프링 부트 정상 기동 로그 확인:
  ```
  Started FinalApplication in X.XXX seconds (JVM running for X.XXX)
  ```
- [ ] 인스턴스가 **Running** 상태인지 확인 (Stopped 상태면 Start 필요)

### MySQL(final-db) 컨테이너가 계속 재시작되는 경우

- [ ] `docker logs final-db` 에러 메시지 확인
- [ ] `DB_PASSWORD` Secret에 특수문자(`!`, `@`, `#` 등)가 포함된 경우 MySQL 호환 여부 확인
  - 안전한 비밀번호 예시: `MyPassword123` (특수문자 없이 영문+숫자 조합)
- [ ] EC2 디스크 공간 확인: `df -h` (t2.micro 기본 8GB — MySQL 데이터로 꽉 찰 수 있음)

---

## 📋 체크리스트 요약 (Quick Reference)

```
PHASE 1: AWS EC2 인스턴스 생성
  [ ] 1-1. AWS 계정 생성 및 콘솔 로그인
  [ ] 1-2. EC2 인스턴스 생성 (Ubuntu 22.04, t2.micro, .pem 키 다운로드)
  [ ] 1-3. 보안 그룹 Inbound Rules에 8080 TCP 포트 추가
  [ ] 1-4. EC2 Public IP 기록 (Elastic IP 연결 권장)
  [ ] 1-5. 로컬에서 SSH 접속 성공 확인

PHASE 2: EC2 서버 환경 세팅 (SSH 접속 후)
  [ ] 2-1. apt update & upgrade
  [ ] 2-2. Docker 설치 + ubuntu 그룹 추가
  [ ] 2-3. Docker Compose 플러그인 설치
  [ ] 2-4. Docker 자동 시작 설정 + docker ps로 정상 확인

PHASE 3: GitHub Repository Secrets 등록 (포크 레포 Settings)
  [ ] SSH_HOST      ← EC2 Public IP
  [ ] SSH_USERNAME  ← ubuntu (고정)
  [ ] SSH_KEY       ← .pem 파일 전체 텍스트 (BEGIN~END 포함)
  [ ] DB_USERNAME   ← MySQL 유저명 (자유 설정)
  [ ] DB_PASSWORD   ← MySQL 비밀번호 (강력하게, 특수문자 주의)
  [ ] JWT_SECRET_KEY← openssl rand -base64 64 생성값
  [ ] Actions 쓰기 권한 (Read and write permissions) 활성화

PHASE 4: 첫 배포 및 검증
  [ ] week8/** 브랜치 확인 또는 생성
  [ ] git push로 GitHub Actions 트리거
  [ ] Actions 탭에서 build → deploy 두 잡 모두 성공(✅) 확인
  [ ] EC2 SSH 접속 후 docker ps로 final-app, final-db 실행 확인
  [ ] http://{EC2_PUBLIC_IP}:8080/swagger-ui.html 브라우저 접속 성공 확인
```

---

> ✅ 위 4단계를 완료하면 `week8/**` 브랜치에 푸시할 때마다 AWS EC2 서버에 최신 코드가 자동으로 배포됩니다.  
> 📌 AWS Free Tier는 12개월 무료이므로, 프로젝트 완료 후 EC2 인스턴스를 **Stop 또는 Terminate**하여 추가 과금을 방지하세요.
