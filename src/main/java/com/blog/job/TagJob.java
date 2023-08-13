package com.blog.job;

import com.blog.services.TagService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TagJob {
    private TagService tagService;

    public TagJob(TagService tagService) {
        this.tagService = tagService;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void analyzeTag() {
        tagService.analyzeTag();
    }
}
