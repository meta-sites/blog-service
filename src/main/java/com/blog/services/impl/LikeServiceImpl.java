package com.blog.services.impl;

import com.blog.dto.LikeDto;
import com.blog.exception.ArticleException;
import com.blog.exception.UserException;
import com.blog.models.Like;
import com.blog.models.User;
import com.blog.repositories.ArticleRepository;
import com.blog.repositories.LikeRepository;
import com.blog.services.AuthenticationService;
import com.blog.services.CacheService;
import com.blog.services.LikeService;
import com.blog.util.ExceptionConstants;
import com.blog.util.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;
    private ArticleRepository articleRepository;
    private AuthenticationService authenticationService;

    public LikeServiceImpl(LikeRepository likeRepository,
                           ArticleRepository articleRepository,
                           AuthenticationService authenticationService) {
        this.likeRepository = likeRepository;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional
    public LikeDto insert(LikeDto dto) throws JsonProcessingException, ArticleException, UserException {
        if (!authenticationService.isLogin())
            throw new UserException(ExceptionConstants.USER_IS_NOT_LOGIN, HttpStatus.BAD_REQUEST);
        Like like = createLikeEntityForInsert(dto);
        Like entity = likeRepository.save(like);
        articleRepository.updateLikeCount(dto.getArticleId(), true);
        return createLikeDto(entity);
    }

    @Override
    @Transactional
    public Boolean remove(LikeDto dto) throws JsonProcessingException {
        User user = authenticationService.getCurrentUser();
        likeRepository.disLikeArticle(dto.getArticleId(), user.getId());
        articleRepository.updateLikeCount(dto.getArticleId(), false);
        return Boolean.TRUE;
    };

    @Override
    public Boolean isLike(String articleId, String userId){
        return likeRepository.isLike(articleId, userId);
    };

    private Like createLikeEntityForInsert(LikeDto dto) throws JsonProcessingException {
        Like like = MapperUtil.map(dto, Like.class);
        like.setCreateBy(authenticationService.getCurrentUser().getId());

        return like;
    }

    private LikeDto createLikeDto(Like like) throws JsonProcessingException {
        LikeDto dto = MapperUtil.map(like, LikeDto.class);
        dto.setCreateBy(authenticationService.getCurrentUser().getName());
        return dto;
    }
}
