package com.example.JWT.global.initData;

import com.example.JWT.domain.article.service.ArticleService;
import com.example.JWT.domain.member.entity.Member;
import com.example.JWT.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(ArticleService articleService, MemberService memberService, PasswordEncoder passwordEncoder) {
        return args -> {
            String password = passwordEncoder.encode("1234");
            Member user1 = memberService.join("user1", password, "test1@test.com");
            Member user2 = memberService.join("user2", password, "test2@test.com");
            Member admin = memberService.join("admin", password, "admin@test.com");

            articleService.create(user1,"제목 1", "내용 1");
            articleService.create(user1,"제목 2", "내용 2");
            articleService.create(user2,"제목 3", "내용 3");
            articleService.create(user2, "제목 4", "내용 4");
            articleService.create(admin,"제목 5", "내용 5");
        };
    }
}