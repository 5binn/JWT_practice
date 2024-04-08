package com.example.JWT.domain.article.controller;

import com.example.JWT.domain.article.entity.Article;
import com.example.JWT.domain.article.service.ArticleService;
import com.example.JWT.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //문자열로 리턴
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ApiV1ArticleController {
    private final ArticleService articleService;


    @AllArgsConstructor
    @Getter
    public static class ArticleListResponse {
        private final List<Article> articleList;
    }

    @GetMapping("")
    public RsData<ArticleListResponse> getArticles() {
        RsData<List<Article>> articleList = this.articleService.getArticleList();
        return RsData.of(
                articleList.getResultCode(),
                articleList.getMsg(),
                new ArticleListResponse(articleList.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class ArticleResponse {
        private final Article article;
    }

    @GetMapping("/{id}")
    public RsData<ArticleResponse> getArticle(@PathVariable("id") Long id) {
        RsData<Article> article = this.articleService.getArticleById(id);
        return RsData.of(
                article.getResultCode(),
                article.getMsg(),
                new ArticleResponse(article.getData())
        );
    }

    @Getter
    @Setter
    public static class ArticleForm {
        @NotBlank(message = "제목 입력 X")
        private String title;
        @NotBlank(message = "내용 입력 X")
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class CreateResponse {
        private final Article article;
    }

    @PostMapping("")
    public RsData<CreateResponse> createArticle(@Valid @RequestBody ArticleForm articleForm) {

        RsData<Article> createArticle = this.articleService.create(null,articleForm.getTitle(), articleForm.getContent());

        if (createArticle.isFail()) {
            return (RsData) createArticle;
        }
        return RsData.of(
                createArticle.getResultCode(),
                createArticle.getMsg(),
                new CreateResponse(createArticle.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class DeleteResponse {
        private final Article article;
    }

    @DeleteMapping("/{id}")
    public RsData<DeleteResponse> deleteArticle(@PathVariable("id") Long id) {
        RsData<Article> deleteArticle = this.articleService.delete(id);
        if (deleteArticle.isFail()) {
            return (RsData) deleteArticle;
        }
        return RsData.of(
                deleteArticle.getResultCode(),
                deleteArticle.getMsg(),
                new DeleteResponse(deleteArticle.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class UpdateResponse {
        private final Article article;
    }

    @PatchMapping("/{id}")
    public RsData<UpdateResponse> updateArticle(@PathVariable("id") Long id, @Valid @RequestBody ArticleForm articleForm) {
        RsData<Article> updateArticle = articleService.update(id, articleForm.getTitle(), articleForm.getContent());
        if (updateArticle.isFail()) {
            return (RsData) updateArticle;
        }
        return RsData.of(
                updateArticle.getResultCode(),
                updateArticle.getMsg(),
                new UpdateResponse(updateArticle.getData())
        );
    }
}
