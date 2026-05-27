이 YAML은 Docker Hub가 아니라 GHCR(GitHub Container Registry)에 push하고, 그다음 원격 서버(질문상 EC2일 가능성이 높음)에서 GHCR 이미지를 pull해서 재배포하는 구조입니다. docker/login-action의 registry: ghcr.io 설정과 이미지 태그 ghcr.io/...가 그 근거입니다.

어디에 push하나
Docker Hub를 쓰려면 보통 이미지 이름이 사용자명/레포명:태그 형태이고, docker push 사용자명/레포명처럼 올라갑니다.

그런데 당신의 YAML은 이미지 태그가 ghcr.io/${owner}/${repo_name}:latest 입니다. 즉 업로드 대상이 Docker Hub가 아니라 GitHub의 컨테이너 레지스트리(GHCR) 입니다.

또한 로그인 대상도 ghcr.io라서, docker/build-push-action이 빌드한 이미지를 GHCR로 밀어 넣습니다.

어디서 pull하나
appleboy/ssh-action으로 원격 서버에 SSH 접속한 뒤, 서버 내부에서 다시 docker login ghcr.io를 실행하고 있습니다. 즉 서버가 직접 GHCR에서 이미지를 pull하는 방식입니다.

이어서 docker compose pull과 docker compose up -d를 실행하므로, 서버의 docker-compose.yml에 적힌 이미지 ghcr.io/...:latest를 받아와 컨테이너를 재기동합니다.

그래서 흐름은 GitHub Actions runner에서 빌드 및 push → 원격 서버에서 pull 및 실행입니다.

전체 흐름
build 잡에서 Gradle 빌드와 테스트를 수행합니다.

deploy 잡에서 Docker 이미지를 빌드합니다.

그 이미지를 ghcr.io/...:latest로 GHCR에 push합니다.

SSH로 서버에 접속합니다.

서버에서 GHCR 로그인 후 docker compose pull을 실행합니다.

docker compose up -d로 앱과 MySQL 컨테이너를 실행하거나 갱신합니다.

헷갈리기 쉬운 점
“EC2에서 pull하는가?” → 네, 서버가 EC2라면 EC2에서 pull하는 구조입니다. 다만 정확히는 Docker Hub가 아니라 GHCR에서 pull합니다.

“EC2에서 빌드하는가?” → 아니요. 빌드는 GitHub Actions 쪽에서 하고, 서버는 이미 만들어진 이미지를 받아 실행만 합니다. 이런 방식은 보통 서버 자원을 아끼고 배포를 단순하게 만드는 데 유리합니다.

“docker-compose.yml은 어디 있나?” → 서버 접속 후 cat << 'EOF' > docker-compose.yml로 서버 안에서 인라인 생성하고 있습니다. 즉 레포 파일을 복사하는 방식이 아니라, 배포 스크립트가 서버에 직접 작성합니다.

한 줄로 정리
이 파이프라인은 Docker Hub push → EC2 pull이 아니라,

GHCR push → 원격 서버(EC2일 수 있음) pull → docker compose로 재배포입니다.

---

보통은 둘 다 많이 쓰지만, 용도가 꽤 갈립니다. 공개 배포와 범용성에서는 Docker Hub가 여전히 매우 익숙하고 널리 쓰이며, GitHub 중심 개발팀의 CI/CD에서는 GHCR을 많이 선택합니다.

많이 쓰는 쪽
Docker Hub는 가장 오래되고 대중적인 컨테이너 레지스트리로 소개되는 경우가 많아서, 튜토리얼·예제·공개 이미지 배포에서는 여전히 존재감이 큽니다.

GHCR은 GitHub Actions, GitHub 저장소, 패키지 권한 관리와 잘 붙기 때문에 GitHub 기반 팀에서 채택이 늘어난다고 자주 언급됩니다.

그래서 “전체 생태계에서 누가 더 유명하냐”로 보면 Docker Hub, “GitHub 프로젝트에서 실무 CI/CD에 뭘 자주 쓰냐”로 보면 GHCR 쪽이 자연스러운 경우가 많습니다.

이렇게 구분하면 쉬움
Docker Hub를 많이 쓰는 경우: 공개 이미지를 널리 배포하고 싶을 때, 사용자들이 가장 먼저 찾는 기본 레지스트리를 쓰고 싶을 때.

GHCR을 많이 쓰는 경우: 소스코드가 GitHub에 있고, GitHub Actions로 빌드/배포 자동화를 묶고 싶을 때, 저장소와 이미지 권한을 함께 관리하고 싶을 때.

특히 개인 프로젝트나 학생 프로젝트에서 GitHub Actions를 이미 쓰고 있다면, 추가 계정보다 GITHUB_TOKEN 기반으로 연결되는 GHCR이 훨씬 편하다고 느끼는 경우가 많습니다.

실무 감각으로 보면
오픈소스나 공개용 이미지는 Docker Hub에 올려두는 사례가 많고, 일부 프로젝트는 Docker Hub와 GHCR 둘 다 운영합니다.

반대로 내부용, 개인용, GitHub 리포지토리와 강하게 연결된 배포 파이프라인은 GHCR 쪽이 더 자연스럽다는 평가가 많습니다.

즉 “무조건 하나가 정답”은 아니고, 배포 대상이 누구냐와 개발 워크플로가 어디에 붙어 있냐가 선택 기준입니다.

너한테 더 어울리는 쪽
네가 올린 YAML처럼 이미 GitHub Actions를 쓰고 있다면, 초보자 입장에서는 GHCR이 더 단순합니다. 로그인 대상도 GitHub이고, 이미지도 GitHub 생태계 안에서 관리되기 때문입니다.

반면 포트폴리오용으로 “누구나 쉽게 pull 가능한 공개 이미지”를 강조하고 싶다면 Docker Hub도 여전히 좋은 선택입니다.

그래서 학생 프로젝트나 개인 백엔드 프로젝트 기준으로는, 처음엔 GHCR, 공개 배포 가시성을 높이고 싶으면 Docker Hub 추가가 실용적인 조합입니다.

