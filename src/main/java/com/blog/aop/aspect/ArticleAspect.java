package com.blog.aop.aspect;

import com.blog.aop.advice.ArticleAction;
import com.blog.repositories.ArticleRepository;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Aspect
@Component
public class ArticleAspect {
    private ArticleRepository articleRepository;
    private AsyncTaskExecutor asyncTaskExecutor;
    private ArticleAction articleAction;

    @Autowired
    public ArticleAspect(ArticleRepository articleRepository, AsyncTaskExecutor asyncTaskExecutor,
                         ArticleAction articleAction) {
        this.articleRepository = articleRepository;
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.articleAction = articleAction;
    }

    private Object[] getArgs(JoinPoint joinPoint){
        return joinPoint.getArgs();
    }

    @AfterReturning("@annotation(com.blog.aop.anotation.IncreaseViewArticle)")
    public void increaseViewAfterMethodExecution(JoinPoint joinPoint) {
        Object[] args = getArgs(joinPoint);
        if (args.length > 0 && args[0] instanceof String) {
            String urlFriendly = (String) args[0];
            if (StringUtils.isNotBlank(urlFriendly)) {
                CompletableFuture.runAsync(() -> articleAction.increaseArticleView(urlFriendly), asyncTaskExecutor);
            }
        }
    }

    @AfterReturning("@annotation(com.blog.aop.anotation.IncreaseShareArticle)")
    public void increaseShareAfterMethodExecution(JoinPoint joinPoint) {
        Object[] args = getArgs(joinPoint);
        if (args.length > 0 && args[0] instanceof String) {
            String id = (String) args[0];
            if (StringUtils.isNotBlank(id)) {
                CompletableFuture.runAsync(() -> articleAction.increaseArticleShare(id), asyncTaskExecutor);
            }
        }
    }
}
