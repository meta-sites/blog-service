package com.blog.aop.advice;

import com.blog.models.Article;
import com.blog.models.PdfFile;
import com.blog.repositories.ArticleRepository;
import com.blog.repositories.PdfFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookAction {

    private AsyncTaskExecutor asyncTaskExecutor;
    private PdfFileRepository pdfFileRepository;

    @Autowired
    public BookAction(AsyncTaskExecutor asyncTaskExecutor, PdfFileRepository pdfFileRepository) {
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.pdfFileRepository = pdfFileRepository;
    }

    @Async("asyncTaskExecutor")
    public void increaseBookSubscribe(String id) {
        Optional< PdfFile > pdfFile = pdfFileRepository.findById(id);
        if (pdfFile.isPresent()) {
            PdfFile entity = pdfFile.get();
            entity.setNumSub(entity.getNumSub() + 1);
            pdfFileRepository.save(entity);
        }
    }
}
