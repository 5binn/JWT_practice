package com.example.JWT;

import com.example.JWT.global.jwt.JwtProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


@SpringBootTest
class JwtApplicationTests {

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${custom.jwt.secretKey}")
    private String originSecretKey;

    @Test
    @DisplayName("시크릿 키 검증")
    void checkKey() {
        Assertions.assertNotNull(originSecretKey);
        System.out.println(originSecretKey);
    }

    @Test
    @DisplayName("기존 시크릿 키 암호화하기")
    void test1() {
        String key = Base64.getEncoder().encodeToString(originSecretKey.getBytes());

        SecretKey secretKey1 = Keys.hmacShaKeyFor(key.getBytes());

        assertNotNull(secretKey1);
        System.out.println(secretKey1);
    }

    @Test
    @DisplayName("시크릿 키 객체생성")
    void test2() {
        SecretKey secretKey = jwtProvider.getSecretKey();
        assertNotNull(secretKey);
    }

    @Test
    @DisplayName("객체 한 번만 생성")
    void test3() {
        SecretKey secretKey1 = jwtProvider.getSecretKey();
        SecretKey secretKey2 = jwtProvider.getSecretKey();
        assertEquals(secretKey1 ,secretKey2);
    }

    @Test
    @DisplayName("액세스 토큰 발급")
    void test4() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1L);
        claims.put("username", "user1");

//        String token = jwtProvider.generateToken(claims, 60 * 60 * 5);
//
//        System.out.println("<<" + token + ">>");
//
//        assertNotNull(token);
    }

    @Test
    @DisplayName("만료된 토큰 유효하지 않은지")
    void test5() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 3L);
        claims.put("username", "admin");

//        // 유효성 시간 x
//        String accessToken = jwtProvider.generateToken(claims, -1);
//
//        System.out.println("accessToken :" + accessToken);
//
//        Assertions.assertFalse(jwtProvider.verify(accessToken));
    }
    @Test
    @DisplayName("access Token을 이용하여 claims 정보 가져오기")
    void test6() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 2L);
        claims.put("username", "admin");

//        // 10분
//        String accessToken = jwtProvider.generateToken(claims, 60 * 10);
//
//        System.out.println("accessToken :" + accessToken);
//
//        assertTrue(jwtProvider.verify(accessToken));
//
//        Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);
//        System.out.println("claimsFromToken : " + claimsFromToken);
    }
}
