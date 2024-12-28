package com.example.userLogin.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.example.userLogin.jwt.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final CustomUserDetails customUserDetails;

    // 원래 Base64로 인코딩 된 문자열 형태의 키
    @Value("${jwt.secretKey}")
    private String secretkey;

    // 디코딩 후, JWT 서명에 실제로 사용되는 키 객체
    private Key key;

    @PostConstruct
    private void initializeKey() {
        // secretKey 디코딩 => Key 객체 초기화
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // AT 생성
    public String createAccessToken(String username, String authorities) {
        return createToken(username, authorities, ACCESS_TOKEN_EXPIRES_TIME);
    }

    // RT 생성
    public String createRefreshToken(String username) {
        return createToken(username, null, REFRESH_TOKEN_EXPIRES_TIME);
    }

    // 토큰 생성
    public String createToken(String username, String authorities, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256);

        if (authorities != null) {
            builder.claim("authorities", authorities);
        }

        return builder.compact();
    }

    // 토큰 발급
    public JwtToken issueToken(Authentication authentication) {
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = createAccessToken(username, authorities);
        String refreshToken = createRefreshToken(username);

        return JwtToken.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // AT 재발급
    public JwtToken reissueToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);

        String username = claims.getSubject();
        String authorities = (String) claims.get("authorities");

        String newAccessToken = createAccessToken(username, authorities);
        String newRefreshToken = createRefreshToken(username);

        return JwtToken.builder()
                .grantType(GRANT_TYPE)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("RefreshToken");
        if(authorizationHeader != null) {
            return authorizationHeader;
        }
        return null;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String username = claims.getSubject();

        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }
}
