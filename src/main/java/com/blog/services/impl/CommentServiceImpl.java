package com.blog.services.impl;

import com.blog.dto.CommentDto;
import com.blog.dto.UserDto;
import com.blog.exception.ArticleException;
import com.blog.models.Comment;
import com.blog.repositories.CommentRepository;
import com.blog.services.AuthenticationService;
import com.blog.services.CommentService;
import com.blog.util.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private AuthenticationService authenticationService;

    public CommentServiceImpl(CommentRepository commentRepository, AuthenticationService authenticationService) {
        this.commentRepository = commentRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public CommentDto insert(CommentDto dto) throws JsonProcessingException, ArticleException {
        Comment comment = createCommentEntityForInsert(dto);
        Comment entity = commentRepository.save(comment);
        return createCommentDto(entity);
    }

    @Override
    public Map<String, Long> countCommentByListArticleId(List<String> ids) {
        List<Object[]> result = commentRepository.countCommentByListArticleId(ids);

        return result.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));
    }
    @Override
    public List<CommentDto> findByArticleId(String id) {
        return commentRepository.findByArticleId(id);
    };

    @Override
    public List<Object[]> countArticleByType(){
        return commentRepository.countArticleByType();
    }

    private CommentDto createCommentDto(Comment comment) throws JsonProcessingException {
        CommentDto dto = MapperUtil.map(comment, CommentDto.class);
        dto.setAuthorName(authenticationService.getCurrentUser().getName());
        dto.setImgUrl(authenticationService.getCurrentUser().getImagePath());
        return dto;
    }

    private UserDto removeUserSensitiveInfo(UserDto dto) {
        dto.setPassword(null);
        dto.setToken(null);
        dto.setUserName(null);
        dto.setUserType(null);
        dto.setRole(null);
        return dto;
    }

    private Comment createCommentEntityForInsert(CommentDto dto) throws JsonProcessingException, ArticleException {
        Comment comment = MapperUtil.map(dto, Comment.class);
        comment.setCreateBy(authenticationService.getCurrentUser().getId());

        return comment;
    }
}
