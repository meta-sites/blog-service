package com.blog.aop.aspect;

import com.blog.aop.advice.ArticleAction;
import com.blog.aop.advice.BookAction;
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
public class BookAspect {

    private AsyncTaskExecutor asyncTaskExecutor;
    private BookAction bookAction;

    @Autowired
    public BookAspect(AsyncTaskExecutor asyncTaskExecutor, BookAction bookAction) {
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.bookAction = bookAction;
    }

    private Object[] getArgs(JoinPoint joinPoint){
        return joinPoint.getArgs();
    }

    @AfterReturning("@annotation(com.blog.aop.anotation.IncreaseSubscribeBook)")
    public void increaseSubscribe(JoinPoint joinPoint) {
        Object[] args = getArgs(joinPoint);
        if (args.length > 0 && args[0] instanceof String) {
            String id = (String) args[0];
            if (StringUtils.isNotBlank(id)) {
                CompletableFuture.runAsync(() -> bookAction.increaseBookSubscribe(id), asyncTaskExecutor);
            }
        }
    }
}
