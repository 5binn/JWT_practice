package com.example.JWT.global.jwt;

import com.example.JWT.domain.member.entity.Member;
import com.example.JWT.global.util.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    private SecretKey cachedSecretKey;
    @Value("${custom.jwt.secretKey}")
    private String originSecretKey;

    private SecretKey _getSecretKey() {
        String key = Base64.getEncoder().encodeToString(originSecretKey.getBytes());
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) {
            cachedSecretKey = _getSecretKey();
        }
        return cachedSecretKey;
    }



    public String generateToken(Member member, int seconds) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("username", member.getUsername());
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L + seconds);

        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateAccessToken(Member member) {
        return generateToken(member, 60 * 10);
    }

    public String generateRefreshToken(Member member) {
        return generateToken(member, 60 * 60 * 24 * 365);
    }

    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Map<String, Object> getClaims(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);
        return Util.toMap(body);
    }


}
