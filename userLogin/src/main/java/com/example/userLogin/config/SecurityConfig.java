package com.example.userLogin.config;

import com.example.userLogin.jwt.JwtAuthenticationFilter;
import com.example.userLogin.jwt.JwtTokenProvider;
import com.example.userLogin.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {


    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder  =
                (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());

        return delegatingPasswordEncoder;
    }

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring()
                .requestMatchers("/error");
    }

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtTokenProvider);
        loginFilter.setFilterProcessesUrl("/login");
        return loginFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        http.oauth2Login(oauth2Login -> oauth2Login
                .clientRegistrationRepository(clientRegistrationRepository())  // OAuth2 클라이언트 등록 정보 설정
                .permitAll());  // 로그인 관련 URL 모두 허용

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login/kakao").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html").permitAll()
                .anyRequest().permitAll() // 모든 요청에 대해 접근 허용
        );

        http.addFilterAt(new LoginFilter(authenticationManager(), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

    // OAuth2 클라이언트 등록을 위한 Bean 추가 (InMemory 설정 예시)
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration kakaoClientRegistration = ClientRegistration.withRegistrationId("kakao")
                .clientId(kakaoClientId) // 환경변수로 대체
                .clientSecret(kakaoClientSecret) // 환경변수로 대체
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .clientName("Kakao")
                .redirectUri("http://localhost:8080/callback")
                .scope("account_email")
                .userNameAttributeName("id")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();

        return new InMemoryClientRegistrationRepository(kakaoClientRegistration);

    }
}
