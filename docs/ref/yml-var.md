docker-compose.yml 과 스프링  application.yml 이 변수를 주입받고
처리하는 흐름은 실무 표준 보안 패턴을 따르고 있으며, **환경에 따라
변수를 읽어오는 원천(Source)**이 다릅니다.

──────
### 1.  docker-compose.yml 의  ${DB_PASSWORD} 는 어디서 읽나요?
도커 컴포즈는 기동될 때 두 가지 경로에서 환경 변수 값을 찾아서
대입합니다.
• 로컬 개발 환경:
도커 컴포즈를 실행하는 운영체제(OS)의 환경 변수 또는 프로젝트 루트
폴더에 존재하는  .env  파일에서 읽어옵니다. (로컬 테스트 시 루트 폴더에
.env  파일을 만들어 환경 변수를 적어두면 도커 컴포즈가 자동으로
읽어갑니다.)
• 상용 배포 환경 (Oracle Cloud + GitHub Actions):
우리가 작성한 deploy.yml의 CD 배포 스크립트를 보시면 다음과 같은
파트가 있습니다:
# GitHub Secrets에서 보안 비밀값을 주입받아 서버 내부에 실시간으로 .env 파일 생성
echo "DB_USERNAME=${{ secrets.DB_USERNAME }}" > .env
echo "DB_PASSWORD=${{ secrets.DB_PASSWORD }}" >> .env
echo "JWT_SECRET_KEY=${{ secrets.JWT_SECRET_KEY }}" >> .env
GitHub의 Secrets에 등록된 암호화된 비밀 값들이 배포 시점에 서버 내에  .
env  파일로 실시간 작성되고,  docker compose up  명령어가 실행되면서 이
.env  파일 속 변수들이  ${DB_PASSWORD}  자리에 쏙쏙 주입됩니다.
──────
### 2. 스프링  application.yml 의  ${DB_USERNAME} 은 어디서 읽나요?
스프링 부트는 구동될 때 자신이 실행되고 있는 OS 시스템 환경 변수에서
플레이스홀더( username: ${DB_USERNAME} )의 값을 파싱하여 채웁니다.

하지만 컨테이너 내부의 스프링은 외부 리눅스 서버의 환경 변수를 직접 볼
수 없으므로, 도커 컴포즈가 가교(Bridge) 역할을 해줍니다.
docker-compose.yml  내부의  app  서비스 설정을 다시 보시면 다음과 같이
정의되어 있습니다:
app:
# (중략)
environment:
# 외부 리눅스(.env)에서 가져온 ${DB_USERNAME} 값을
# 도커 컨테이너 내부 OS 환경 변수인 DB_USERNAME으로 주입해
줍니다.
DB_USERNAME: ${DB_USERNAME}
DB_PASSWORD: ${DB_PASSWORD}
JWT_SECRET_KEY: ${JWT_SECRET_KEY}

#### 🔄 전체 전파 흐름 요약:

1. GitHub Repository Secrets 혹은 로컬의  .env  파일의 변수 확보
2. 도커 컴포즈가 변수를 파싱하여  docker-compose.yml  내부의
   ${DB_USERNAME} 을 치환
3. 치환된 값을 기반으로 스프링 부트 컨테이너 내부의 OS 환경 변수로 전달
   ( environment:  지시어 기능)
4. 컨테이너 안에서 가동 중인 스프링 부트가 본인의 환경 변수를 감지하여
   application.yml  플레이스홀더 최종 완성!
   ──────
   이러한 단계별 주입 흐름 덕분에 리포지토리의 소스 코드(yml 등)에 보안
   유출 위험이 높은 비밀번호를 단 한 글자도 하드코딩하지 않고, GitHub
   Secrets 하나로 안전하게 통제 및 자동 배포할 수 있는 안전한 실무 보안
   설계가 완성되는 것입니다.