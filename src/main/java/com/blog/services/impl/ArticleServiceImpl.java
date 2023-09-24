package com.blog.services.impl;

import com.blog.aop.anotation.IncreaseShareArticle;
import com.blog.aop.anotation.IncreaseViewArticle;
import com.blog.dto.ArticleDto;
import com.blog.dto.ArticleSearchDto;
import com.blog.dto.ArticleShortDto;
import com.blog.dto.SummaryArticleDto;
import com.blog.dto.PdfFileDto;
import com.blog.dto.CommentDto;
import com.blog.enums.ArticleEnum;
import com.blog.exception.ArticleException;
import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.models.Article;
import com.blog.models.EmailPreference;
import com.blog.models.User;
import com.blog.repositories.ArticleRepository;
import com.blog.services.ArticleService;
import com.blog.services.AuthenticationService;
import com.blog.services.CommentService;
import com.blog.services.LikeService;
import com.blog.services.ResourceService;
import com.blog.services.MailService;
import com.blog.services.EmailPreferenceService;
import com.blog.util.ExceptionConstants;
import com.blog.util.UtilFunction;
import com.blog.util.MapperUtil;
import com.blog.util.FileUtil;
import com.blog.util.Constants;
import com.blog.util.ResourceConstants;
import com.blog.util.MailConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService  {

    @Value("${article.cover.dir}")
    private String articleCoverDir;

    private ArticleRepository articleRepository;
    private AuthenticationService authenticationService;
    private CommentService commentService;
    private LikeService likeService;
    private ResourceService resourceService;
    private MailService mailService;
    private EmailPreferenceService emailPreferenceService;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              AuthenticationService authenticationService, CommentService commentService,
                              LikeService likeService, ResourceService resourceService, MailService mailService,
                              EmailPreferenceService emailPreferenceService) {
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.resourceService = resourceService;
        this.mailService = mailService;
        this.emailPreferenceService = emailPreferenceService;
    }

    @Override
    public ArticleDto insert(ArticleDto articleDto) throws IOException {
        Article article = createArticleEntityForInsert(articleDto);
        Article entity = articleRepository.save(article);

        sendMailToRecipient(entity);
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
        if (isGetByIsLike(dto)) return setCountComment(getByLike(dto));
        if (isGetByTags(dto)) return setCountComment(getAllTags(dto));
        throw new ArticleException(ExceptionConstants.SEARCH_CONDITION_IS_NOT_PROPER, HttpStatus.BAD_REQUEST);
    }


    @Override
    @IncreaseShareArticle
    public Boolean share(String id) {
        return Boolean.TRUE;
    };

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

    private boolean isGetByIsLike(final ArticleSearchDto dto) {
        return Boolean.TRUE.equals(dto.getIsLike())  && hasPaging(dto);
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

    private List<ArticleShortDto> getByLike(final ArticleSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        String userId = "";
        try {
            userId = authenticationService.getCurrentUser().getId();
        } catch (JsonProcessingException e) {
            log.error("{} {}", UtilFunction.dateToString(new Date()), e.getMessage());
        }

        return articleRepository.getByLike(pageable, userId);
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
    public Boolean deleteArticleImage(String fileName) throws IOException {
        String filePath = articleCoverDir + fileName;
        resourceService.removeResource(filePath);
        log.info("Xóa ảnh đại diện của bài viết: " + fileName);
        return Boolean.TRUE;
    }

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
                        UtilFunction.stringToDate(String.valueOf(result[9])), Long.valueOf(String.valueOf(result[10])));
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
        entity.setNumLike(NumberUtils.LONG_ZERO);
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

    private void sendMailToRecipient(Article article) throws IOException {
        Context context = new Context();
        context.setVariable("title", article.getTitle());
        context.setVariable("shortDesc", article.getDescription());
        context.setVariable("imageURL", buildImageUrl(article));
        context.setVariable("link", buildLinkArticle(article));

        List< EmailPreference > emailPreferenceList = emailPreferenceService.findEmailPreferencesByVerified();
        emailPreferenceList.parallelStream().forEach( emailPreference -> {
            try {
                context.setVariable("unsubscribeLink", emailPreferenceService.generateUnsubscribe(emailPreference.getEmail()));
                mailService.sendEmailWithTemplate(emailPreference.getEmail(), article.getTitle(), MailConstant.NEWS_ARTICLE_TEMPLATE, context);
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Can not send email to " + emailPreference.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } );
    }

    private String buildLinkArticle(Article article) throws IOException {
        return new StringBuilder(resourceService.getResourcePath(ResourceConstants.SERVER_DOMAIN_URL))
                .append(Constants.SLASH)
                .append("blog")
                .append(Constants.SLASH)
                .append(article.getType().toString().toLowerCase())
                .append(Constants.SLASH)
                .append(article.getUrlFriendly())
                .toString();
    }

    private String buildImageUrl(Article article) throws IOException {
        return new StringBuilder(resourceService.getResourcePath(ResourceConstants.SERVER_DOMAIN_URL))
                .append("/public/api/article/article-image-cover/")
                .append(article.getImageUrl())
                .toString();
    }
}
