package com.blog.aop.advice;

import com.blog.models.Article;
import com.blog.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class ArticleAction {

    private ArticleRepository articleRepository;
    private AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    public ArticleAction(ArticleRepository articleRepository, AsyncTaskExecutor asyncTaskExecutor) {
        this.articleRepository = articleRepository;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Async("asyncTaskExecutor")
    public void increaseArticleView(String urlFriendly) {
        Article article =  articleRepository.findByUrlFriendly(urlFriendly);
        if (Objects.nonNull(article)) {
            article.setNumViews(article.getNumViews() + 1);
            articleRepository.save(article);
        }
    }

    @Async("asyncTaskExecutor")
    public void increaseArticleShare(String id) {
        Optional<Article> article =  articleRepository.findById(id);
        if (article.isPresent()) {
            Article entity = article.get();
            entity.setNumShares(entity.getNumShares() + 1);
            articleRepository.save(entity);
        }
    }
}
