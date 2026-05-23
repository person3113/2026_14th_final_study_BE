# 1. Build Stage (JDK 환경에서 소스 컴파일 및 빌드)
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

# Gradle Wrapper 및 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# gradlew 실행 권한 부여 및 기본 의존성 사전 캐싱
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 소스 코드 복사 후 Executable Jar 생성 (테스트 스킵으로 속도 극대화)
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# 2. Run Stage (JRE만 탑재된 경량 런타임 환경)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# 빌드 스테이지에서 산출된 jar 파일만 복사하여 이미지 용량 최적화
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너 서비스 포트 노출
EXPOSE 8080

# 상용 운영 프로필(prod)을 활성화하여 구동하도록 진입점 설정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
