package org.userservice.userservice.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.userservice.userservice.error.ErrorCode;
import org.userservice.userservice.error.exception.TokenExpiredException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 클래스:
 * JWT 인증 및 인가 작업을 위한 메서드 모음
 * <p>
 * 설명:
 * Authentication: 소셜 로그인 성공시 SuccessHandler 에서 JWT 를 생성해 프론트로 전달
 * Authorization: permit 이 필요한 엔드포인트에 대해 JWT 검증하여 성공시 인증 객체 및 세션 생성
 * <p>
 * secret: JWT 생성을 위한 비밀키 생성에 사용되는 베이스 문자열
 * secretKey:
 * secret 문자열을 이용해 HS256 알고리즘에 맞게 생성된 객체로 JWT 를 해당알고리즘에 맞게 암호화하여 생성
 * HS256(HMAC-SHA256, 해시알고리즘)
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //Authentication: JWT 생성
    public String createToken(String providerName, String type, String role, Long expireMs) {
        return Jwts.builder()
                .subject(providerName)
                .claim("type", type)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (SignatureException e) {
            log.error("Invalid JWT signature, signature 가 유효하지 않은 토큰 입니다.");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid JWT token, 유효하지 않은 jwt 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token. 만료된 jwt 토큰 입니다.");
            throw new TokenExpiredException(ErrorCode.TOKEN_EXPIRED, "token 만료", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}