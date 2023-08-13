package com.blog.services.impl;

import com.blog.dto.TagsDto;
import com.blog.models.Article;
import com.blog.models.Tags;
import com.blog.repositories.ArticleRepository;
import com.blog.repositories.TagRepository;
import com.blog.services.ArticleService;
import com.blog.services.TagService;
import com.blog.util.Constants;
import com.blog.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    private ArticleRepository articleRepository;
    private TagRepository tagRepository;

    public TagServiceImpl(ArticleRepository articleRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void analyzeTag() {
        Map<String, Long> tagMap = analyzeTagByArticle();
        updateTags(tagMap);
    }

    @Override
    public List< TagsDto > findAll() {
        return MapperUtil.mapAll(tagRepository.findAll(), TagsDto.class);
    };

    private Map<String, Long> analyzeTagByArticle() {
        Map<String, Long> tagMap = new HashMap<>();
        List< Article > articleList = articleRepository.findAll();
        articleList.stream().forEach( article -> {
            String tags = article.getTags();
            List<String> tagList = Arrays.asList(tags.split(Constants.COMMA_CHARACTER));
            for(String tag: tagList) {
                if (tag.trim().length() > 0) {
                    tagMap.putIfAbsent(tag, 0L);
                    tagMap.put(tag, tagMap.get(tag) + 1);
                }
            }
        } );

        return tagMap;
    }

    private void updateTags(Map<String, Long> tagMap) {
        tagMap.entrySet().parallelStream().forEach(tagEntry -> {
            String name = tagEntry.getKey();
            Long appearance = tagEntry.getValue();

            Tags tags = tagRepository.findByName(name)
                    .orElseGet(() -> {
                        Tags newTags = new Tags();
                        newTags.setName(name);
                        return newTags;
                    });

            if (!appearance.equals(tags.getAppearance())) {
                tags.setAppearance(appearance);
                tagRepository.save(tags);
            }
        });
    }

}
