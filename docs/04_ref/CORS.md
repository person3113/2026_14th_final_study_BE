### 1. 현재 백엔드 코드베이스의 CORS 설정 현황

현재 백엔드 코드에는 글로벌 CORS 허용 설정( CorsConfigurationSource  등)이 구성되어
있지 않습니다.
다만, 크로스 오리진 환경에서 쿠키가 정상적으로 전송될 수 있도록 아래와 같은 준비는
되어 있습니다.
•  AuthController.java  (라인 62): 로그인 시 Refresh Token을 담아 반환하는
ResponseCookie 에  .sameSite("None")  및  .secure(true)  옵션이 적용되어 있습니다.
• 이는 프론트엔드와 백엔드의 도메인이 서로 다른 크로스 오리진 환경에서도 브라우저가
쿠키를 안전하게 주고받을 수 있도록 지원하는 설정입니다.
하지만 백엔드 자체에서 특정 오리진(Origin)의 접속을 허용하는 CORS 필터나 Security
설정이 없기 때문에, 프론트엔드 개발 방식에 따라 대응이 달라집니다.
──────
### 2. 프론트엔드 개발 방식에 따른 CORS 해결 전략
#### 💡 방안 A: 프론트엔드 프록시 우회 (3번 & 4번 방식 활용) - 추천

프론트엔드가 개발 및 배포 환경에서 모든  /api  요청을 프록시를 통해 백엔드로
우회하도록 설정한다면, 브라우저 관점에서는 동일 출처(Same-Origin) 통신이 됩니다.
따라서 백엔드에 별도의 CORS 설정을 추가하지 않아도 통신이 가능합니다.

##### ① 로컬 개발 환경 ( vite.config.ts  예시)
Vite 개발 서버의 프록시 설정을 이용해  /api 로 시작하는 요청을 백엔드 포트(
http://localhost:8080 )로 우회시킵니다.

    import { defineConfig } from 'vite';
    import react from '@vitejs/react-refresh';
    export default defineConfig({
      plugins: [react()],
      server: {
        proxy: {
          '/api': {
            target: 'http://localhost:8080',
            changeOrigin: true,
            secure: false,
          },
        },
      },
    });

##### ② Vercel 배포 환경 ( vercel.json  예시)
Vercel로 프론트엔드를 배포할 때, 프로젝트 루트 경로에  vercel.json  파일을 작성하여
/api  요청을 백엔드 실제 운영 서버 주소로 라우팅(Rewrite)하도록 설정합니다.
{
"rewrites": [
{
"source": "/api/:path*",
"destination": "https://your-backend-api-domain.com/api/:path*"
}
]
}
──────
#### 💡 방안 B: 백엔드에서 CORS 직접 허용 설정

만약 프론트엔드에서 프록시 우회를 사용하지 않고, 백엔드 서버 URL(예:
https://api.domain.com )로 직접 HTTP 요청을 보내야 하는 상황이라면, 백엔드의 Spring
Security 설정에 CORS 허용 정책을 추가해야 브라우저의 차단을 막을 수 있습니다.

JWT 쿠키 연동을 위해서는  allowCredentials(true)  설정이 필수적입니다. 아래는
백엔드에 적용할 수 있는 설정 예시입니다.

#####  SecurityConfig.java  수정 예시

    // 1. 필요한 패키지 임포트
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import java.util.List;

    // 2. SecurityConfig 클래스 내부 추가
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                // ... (기존 설정 유지)

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 프론트엔드 오리진 지정 (로컬 및 Vercel 배포 도메인)
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://*.vercel.app" // Vercel 프리뷰 및 프로덕션 도메인 대응
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE",
"OPTIONS"));
configuration.setAllowedHeaders(List.of("*"));
configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));

        // 크레덴셜 허용 (쿠키 및 Authorization 헤더 전송에 필수)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
──────
### 요약 및 제안

• 프론트엔드에서 Vite Proxy 및 Vercel Rewrites를 완벽히 구성하여 배포할 예정이라면,
현재 백엔드 코드 상태 그대로 개발을 진행하셔도 무방합니다. (쿠키의 SameSite 설정이
이미 준비되어 있습니다.)
• 만약 프록시 우회 방식 설정 과정에서 어려움이 예상되거나 직접 API 호출 방식으로
진행하고자 하신다면, 백엔드의  SecurityConfig.java 에 위의 CORS 설정을 추가하시는
것을 권장합니다. 필요한 경우 관련 설정 반영 작업을 지원해 드릴 수 있으니 편하게
말씀해 주세요.