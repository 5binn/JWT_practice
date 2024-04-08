package com.example.JWT.domain.article.service;

import com.example.JWT.domain.article.entity.Article;
import com.example.JWT.domain.article.repository.ArticleRepository;
import com.example.JWT.domain.member.entity.Member;
import com.example.JWT.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public RsData<List<Article>> getArticleList() {
        List<Article> articleList = this.articleRepository.findAll();
        if (articleList.isEmpty()) {
            return RsData.of("F-1", "데이터 없음", null);
        }
        return RsData.of("S-1", "성공", articleList);
    }

    public RsData<Article> getArticleById(Long id) {
        return this.articleRepository.findById(id).map((article) -> RsData.of(
                "S-2",
                "성공",
                article
        )).orElseGet(() -> RsData.of(
                "F-2",
                "%d 번 게시물 없음".formatted(id),
                null
        ));
    }

    @Transactional
    public RsData<Article> create(Member author, String title, String content) {
        try {
            Article article = Article.builder()
                    .author(author)
                    .title(title)
                    .content(content)
                    .build();
            this.articleRepository.save(article);

            return RsData.of(
                    "S-3",
                    "등록 완료",
                    article
            );
        } catch (Exception e) {
            return RsData.of(
                    "F-3",
                    "등록 실패",
                    null
            );
        }
    }

    @Transactional
    public RsData<Article> delete(Long id) {
        RsData<Article> articleRsData = this.getArticleById(id);
        if (articleRsData.isFail()) {
            return articleRsData;
        }
        try {
            this.articleRepository.delete(articleRsData.getData());
            return RsData.of(
                    "S-4",
                    "%d 번 삭제 완료".formatted(id),
                    articleRsData.getData()
            );
        } catch (Exception e) {
            return RsData.of(
                    "F-4",
                    "삭제 실패",
                    null
            );
        }
    }

    @Transactional
    public RsData<Article> update(Long id, String title, String content) {
        RsData<Article> articleRsData = this.getArticleById(id);
        if (articleRsData.isFail()) {
            return articleRsData;
        }
        try {
            Article updateArticle = articleRsData.getData().toBuilder()
                    .title(title)
                    .content(content)
                    .build();
            this.articleRepository.save(updateArticle);
            return RsData.of(
                    "S-5",
                    "수정 완료",
                    updateArticle);
        } catch (Exception e) {
            return RsData.of(
                    "F-5",
                    "수정 실패",
                    null
            );
        }
    }
}
