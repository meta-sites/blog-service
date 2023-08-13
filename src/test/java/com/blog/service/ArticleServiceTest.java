//package com.blog.service;
//
//import com.blog.BaseUnitTest;
//import com.blog.dto.ArticleDto;
//import com.blog.dto.ArticleSearchDto;
//import com.blog.dto.ArticleShortDto;
//import com.blog.exception.ArticleException;
//import com.blog.exception.PasswordDecodeException;
//import com.blog.repositories.ArticleRepository;
//import com.blog.repositories.UserRepository;
//import com.blog.services.impl.ArticleServiceImpl;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.mockito.stubbing.Answer;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//public class ArticleServiceTest extends BaseUnitTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ArticleRepository articleRepository;
//
//    @InjectMocks
//    private ArticleServiceImpl articleService;
//
//    @BeforeEach
//    public void setUp() {
//        Mockito.when(articleRepository.findByUrlFriendly(ARTICLE_GET_ONE)).thenReturn(generateArticleForFindByUrlFriendly());
//        Mockito.when(articleRepository.findByUrlFriendly(ARTICLE_GET_ONE_NOT_FOUND)).thenReturn(Optional.empty());
//        Mockito.when(articleRepository.getFullTextSearch(Mockito.eq(ARTICLE_FULL_TEXT_SEARCH), Mockito.any())).thenReturn(generateArticleList());
//        Mockito.when(articleRepository.getByType(Mockito.eq(ARTICLE_GET_BY_TYPE), Mockito.any())).thenReturn(generateArticleList());
//        Mockito.when(articleRepository.getOrderByViews(Mockito.any())).thenReturn(generateArticleList());
//        Mockito.when(articleRepository.getOrderByTime(Mockito.any())).thenReturn(generateArticleList());
//    }
//
//    @AfterEach
//    public void tearUp() {
//        Mockito.clearAllCaches();
//    }
//
//    @Test
//    void test_filter_find_one_success() throws ArticleException {
//        ArticleSearchDto dto = new ArticleSearchDto();
//        dto.setUrlFriendly(ARTICLE_GET_ONE);
//        List< ArticleShortDto > articleDtoList = articleService.filter(dto);
//        Assertions.assertEquals(  true, articleDtoList.size() == 1);
//        Assertions.assertEquals(  ARTICLE_GET_ONE, articleDtoList.get(0).getTitle());
//    }
//
//    @Test
//    void test_filter_find_one_fail_not_found() {
//        ArticleSearchDto dto = new ArticleSearchDto();
//        dto.setUrlFriendly(ARTICLE_GET_ONE_NOT_FOUND);
//        assertThrows(ArticleException.class, () -> articleService.filter(dto));
//    }
//
//    @Test
//    void test_filter_full_text_search_success() throws ArticleException {
//        ArticleSearchDto dto = new ArticleSearchDto();
//        dto.setTextSearch(ARTICLE_FULL_TEXT_SEARCH);
//        dto.setNumsPerPage(1);
//        dto.setPageNumber(1);
//        List<ArticleDto> articleDtoList = articleService.filter(dto);
//        Assertions.assertEquals(  true, articleDtoList.size() == 1);
//        Assertions.assertEquals(  ARTICLE_TITLE, articleDtoList.get(0).getTitle());
//    }
//
//    @Test
//    void test_filter_get_by_type_success() throws ArticleException {
//        ArticleSearchDto dto = new ArticleSearchDto();
//        dto.setType(ARTICLE_GET_BY_TYPE);
//        dto.setNumsPerPage(1);
//        dto.setPageNumber(1);
//        List<ArticleDto> articleDtoList = articleService.filter(dto);
//        Assertions.assertEquals(  true, articleDtoList.size() == 1);
//        Assertions.assertEquals(  ARTICLE_TITLE, articleDtoList.get(0).getTitle());
//    }
//
//    @Test
//    void test_filter_get_order_by_views_success() throws ArticleException {
//        ArticleSearchDto dto = new ArticleSearchDto();
//        dto.setIsOrderAllByView(true);
//        dto.setNumsPerPage(1);
//        dto.setPageNumber(1);
//        List<ArticleDto> articleDtoList = articleService.filter(dto);
//        Assertions.assertEquals(  true, articleDtoList.size() == 1);
//        Assertions.assertEquals(  ARTICLE_TITLE, articleDtoList.get(0).getTitle());
//    }
//
//    @Test
//    void test_filter_get_order_by_time_success() throws ArticleException {
//        ArticleSearchDto dto = new ArticleSearchDto();
//        dto.setIsOrderAllByTime(true);
//        dto.setNumsPerPage(1);
//        dto.setPageNumber(1);
//        List<ArticleDto> articleDtoList = articleService.filter(dto);
//        Assertions.assertEquals(  true, articleDtoList.size() == 1);
//        Assertions.assertEquals(  ARTICLE_TITLE, articleDtoList.get(0).getTitle());
//    }
//}
