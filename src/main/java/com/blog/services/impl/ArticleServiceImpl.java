package com.blog.services.impl;

import com.blog.aop.anotation.IncreaseShareArticle;
import com.blog.aop.anotation.IncreaseViewArticle;
import com.blog.dto.*;
import com.blog.enums.ArticleEnum;
import com.blog.exception.ArticleException;
import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.models.Article;
import com.blog.models.User;
import com.blog.repositories.ArticleRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.*;
import com.blog.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.parsson.MapUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService  {

    @Value("${article.cover.dir}")
    private String articleCoverDir;

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private AuthenticationService authenticationService;
    private CommentService commentService;
    private LikeService likeService;
    private ResourceService resourceService;

    public ArticleServiceImpl(UserRepository userRepository, ArticleRepository articleRepository,
                              AuthenticationService authenticationService, CommentService commentService,
                              LikeService likeService, ResourceService resourceService) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.resourceService = resourceService;
    }

    @Override
    public ArticleDto insert(ArticleDto articleDto) throws JsonProcessingException {
        Article article = createArticleEntityForInsert(articleDto);
        Article entity = articleRepository.save(article);

        return createArticleDto(entity);
    }

    @Override
    public ArticleDto update(ArticleDto articleDto) throws ArticleException, JsonProcessingException {
        Article article = createArticleEntityForUpdate(articleDto);
        Article entity = articleRepository.save(article);
        return createArticleDto(entity);
    };

    @Override
    public List<ArticleShortDto> filter(final ArticleSearchDto dto) throws ArticleException {
        if (isGetFullTextSearch(dto)) return setCountComment(getFullTextSearch(dto));
        if (isGetByType(dto)) return setCountComment(getByType(dto));
        if (isGetOrderByViews(dto)) return setCountComment(getOrderByViews(dto));
        if (isGetOrderAllByTime(dto)) return setCountComment(getOrderAllByTime(dto));
        if (isGetByTags(dto)) return setCountComment(getAllTags(dto));
        throw new ArticleException(ExceptionConstants.SEARCH_CONDITION_IS_NOT_PROPER, HttpStatus.BAD_REQUEST);
    }


    @Override
    @IncreaseShareArticle
    public Boolean share(String id) {
        return Boolean.TRUE;
    };

    private boolean isGetOne(final ArticleSearchDto dto) {
        return StringUtils.isNotBlank(dto.getUrlFriendly());
    }

    private boolean isGetFullTextSearch(final ArticleSearchDto dto) {
        return StringUtils.isNotBlank(dto.getTextSearch()) && hasPaging(dto);
    }

    private boolean isGetByType(final ArticleSearchDto dto) {
        return Objects.nonNull(dto.getType()) && hasPaging(dto);
    }

    private boolean isGetOrderByViews(final ArticleSearchDto dto) {
        return BooleanUtils.isTrue(dto.getIsOrderAllByView())  && hasPaging(dto);
    }

    private boolean isGetOrderAllByTime(final ArticleSearchDto dto) {
        return BooleanUtils.isTrue(dto.getIsOrderAllByTime()) && hasPaging(dto);
    }

    private boolean isGetByTags(final ArticleSearchDto dto) {
        return StringUtils.isNotBlank(dto.getTags())  && hasPaging(dto);
    }

    private boolean hasPaging(final ArticleSearchDto dto) {
        return Objects.nonNull(dto.getNumsPerPage()) && Objects.nonNull(dto.getPageNumber());
    }

    private List< ArticleShortDto > getOrderByViews(final ArticleSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return articleRepository.getOrderByViews(pageable);
    }

    private List<ArticleShortDto> getOrderAllByTime(final ArticleSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return articleRepository.getOrderByTime(pageable);
    }

    private List<ArticleShortDto> getAllTags(final ArticleSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return articleRepository.getOrderByTags(dto.getTags(), pageable);
    }

    @IncreaseViewArticle
    @Override
    public ArticleDto getOne(String urlFriendly) throws ArticleException, JsonProcessingException {
        Article article = articleRepository.findByUrlFriendly(urlFriendly);
        if (Objects.isNull(article))
            throw new ArticleException(ExceptionConstants.CAN_NOT_FIND_ARTICLE, HttpStatus.NOT_FOUND);
        ArticleDto articleDto = MapperUtil.map(article, ArticleDto.class);
        setArticleComments(articleDto);
        setIsLike(articleDto);
        return articleDto;
    };

    @Override
    public SummaryArticleDto summary() {
        SummaryArticleDto dto = new SummaryArticleDto();
        buildArticleViewAndCountDto(dto, articleRepository.countArticleByType());
        buildCommentCountDto(dto, commentService.countArticleByType());
        return dto;
    };

    @Override
    public PdfFileDto uploadArticleImage(MultipartFile file) throws IOException {
        FileUtil.createDirectoryIfNoExist(articleCoverDir);
        String coverName = UUID.randomUUID().toString() + Constants.PNG_EXTENSION;
        String filePath = articleCoverDir + coverName;
        file.transferTo(Paths.get(filePath));

        PdfFileDto dto = new PdfFileDto();
        dto.setFileName(coverName);
        return dto;
    };

    @Override
    public byte[] getArticleImageCover(String id) throws IOException, FileException, BookException {
        return resourceService.accessResource(articleCoverDir + id);
    };

    private void buildArticleViewAndCountDto(SummaryArticleDto dto, List<Object[]> articleCount) {
        articleCount.stream().forEach(article -> {
            switch (String.valueOf(article[0])) {
                case "JAVA":
                    dto.setNumberArticleJava(Long.valueOf(String.valueOf(article[1])));
                    dto.setNumberViewJava(Long.valueOf(String.valueOf(article[2])));
                    break;
                case "OOP":
                    dto.setNumberArticleOOP(Long.valueOf(String.valueOf(article[1])));
                    dto.setNumberViewOOP(Long.valueOf(String.valueOf(article[2])));
                    break;
                case "SPRING":
                    dto.setNumberArticleSpring(Long.valueOf(String.valueOf(article[1])));
                    dto.setNumberViewSpring(Long.valueOf(String.valueOf(article[2])));
                    break;
                case "DEV_OPS":
                    dto.setNumberArticleDevOP(Long.valueOf(String.valueOf(article[1])));
                    dto.setNumberViewDevOP(Long.valueOf(String.valueOf(article[2])));
                    break;
                default:
                    break;
            }
        });
    }

    private void buildCommentCountDto(SummaryArticleDto dto, List<Object[]> articleCount) {
        articleCount.stream().forEach(comment -> {
            switch (String.valueOf(comment[0])) {
                case "JAVA":
                    dto.setNumberCommentJava(Long.valueOf(String.valueOf(comment[1])));
                    break;
                case "OOP":
                    dto.setNumberCommentOOP(Long.valueOf(String.valueOf(comment[1])));
                    break;
                case "SPRING":
                    dto.setNumberCommentSpring(Long.valueOf(String.valueOf(comment[1])));
                    break;
                case "DEV_OPS":
                    dto.setNumberCommentDevOP(Long.valueOf(String.valueOf(comment[1])));
                    break;
                default:
                    break;
            }
        });
    }

    private void setArticleComments(ArticleDto articleDto) {
        List< CommentDto > commentDto = commentService.findByArticleId(articleDto.getId());
        articleDto.setComments(commentDto);
    }

    private void setIsLike(ArticleDto articleDto) throws JsonProcessingException {
        Boolean isAuthentication = authenticationService.isLogin();
        if (!isAuthentication) return;

        User user = authenticationService.getCurrentUser();
        Boolean isLike = likeService.isLike(articleDto.getId(), user.getId());
        articleDto.setIsLikes(isLike);
    }

    private List<ArticleShortDto> getFullTextSearch(final ArticleSearchDto dto){
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        List<Object[]> results = articleRepository.getFullTextSearch(dto.getTextSearch(), pageable);

        return results.stream().map( result -> {
            try {
                return new ArticleShortDto( String.valueOf(result[0]), String.valueOf(result[1]),
                        String.valueOf(result[2]), String.valueOf(result[3]), String.valueOf(result[4]),
                        Long.valueOf(String.valueOf(result[5])), Long.valueOf(String.valueOf(result[6])),
                        String.valueOf(result[7]), ArticleEnum.valueOf(String.valueOf(result[8])),
                        UtilFunction.StringToDate(String.valueOf(result[9])));
            } catch (ParseException e) {
                return null;
            }
        }).filter( result -> Objects.nonNull(result) ).collect(Collectors.toList());
    }

    private List<ArticleShortDto> setCountComment(List<ArticleShortDto> dtoList ) {
        Map<String, Long> commentCountMap = commentService.countCommentByListArticleId(
                dtoList.stream().map(ArticleShortDto::getId).collect(Collectors.toList()));
        dtoList.forEach( dto -> dto.setNumComments(commentCountMap.getOrDefault(dto.getId(), 0L)) );
        return dtoList;
    }

    private List<ArticleShortDto> getByType(ArticleSearchDto dto){
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return articleRepository.getByType(dto.getType(), pageable);
    }

    private ArticleDto createArticleDto(Article article) {
        ArticleDto dto = MapperUtil.map(article, ArticleDto.class);
        return dto;
    }


    private Article createArticleEntityForInsert(ArticleDto articleDto) throws JsonProcessingException {
        Article entity = MapperUtil.map(articleDto, Article.class);
        entity.setCreateBy(Objects.isNull(articleDto.getCreateAt())
                ? authenticationService.getCurrentUser().getName()
                : articleDto.getCreateBy());

        entity.setNumViews(NumberUtils.LONG_ZERO);
        entity.setNumShares(NumberUtils.LONG_ZERO);
        entity.setUrlFriendly(UtilFunction.createUrlFriendlyFromTitle(entity.getTitle()));
        return entity;
    }

    private Article createArticleEntityForUpdate(ArticleDto articleDto) throws ArticleException, JsonProcessingException {
        String articleId = articleDto.getId();
        Optional<Article> article = articleRepository.findById(articleId);
        if (!article.isPresent())
            throw new ArticleException(ExceptionConstants.CAN_NOT_FIND_ARTICLE, HttpStatus.NOT_FOUND);
        Article entity = article.get();

        entity.setModifiedBy(authenticationService.getCurrentUser().getName());
        entity.setContentsDetail(articleDto.getContentsDetail());
        entity.setTitle(articleDto.getTitle());
        entity.setDescription(articleDto.getDescription());
        entity.setTags(articleDto.getTags());
        entity.setImageUrl(articleDto.getImageUrl());

        return entity;
    }
}
