//package com.blog;
//
//import com.blog.dto.ArticleDto;
//import com.blog.dto.ArticleShortDto;
//import com.blog.dto.UserDto;
//import com.blog.enums.ArticleEnum;
//import com.blog.enums.RoleEnum;
//import com.blog.models.Article;
//import com.blog.models.Token;
//import com.blog.models.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//public class BaseUnitTest {
//    public static final String BLANK_STRING = "";
//    public static final String SHORT_PASSWORD = "short";
//    public static final String PASSWORD = "password_";
//    public static final String NAME = "name";
//    public static final String TOKEN = "token";
//    public static final String EMAIL = "email@email.com";
//    public static final RoleEnum ROLE = RoleEnum.USER;
//    public static final String USER_TYPE = "Normal";
//    public static final String ID = "04cc1194-4a11-4c9d-9b19-f3702b8b8126";
//    public static final String USER_NAME = "userName";
//    public static final String USER_EXIST_TOKEN = "generateUserForLoginExistToken";
//    public static final String USER_NO_EXIST_TOKEN = "generateUserForLoginNoExistToken";
//
//    public static final String USER_HAS_CACHE_TOKEN_EXPIRE = "userHasCacheTokenExpire";
//    public static final String USER_HAS_CACHE_TOKEN_VALID = "userHasCacheTokenValid";
//
//    public static final String ARTICLE_TITLE = "articleTitle";
//    public static final String ARTICLE_GET_ONE = "articleGetOne";
//    public static final String ARTICLE_GET_ONE_NOT_FOUND = "articleGetOneNotFound";
//    public static final String ARTICLE_FULL_TEXT_SEARCH = "articleFullTextSearch";
//    public static final ArticleEnum ARTICLE_GET_BY_TYPE = ArticleEnum.JAVA;
//    public static final String ARTICLE_ORDER_BY_VIEW = "articleOrderByView";
//
//    public UserDto generateUserDtoBlank() {
//        UserDto userDto = new UserDto();
//        userDto.setPassword(BLANK_STRING);
//
//        return userDto;
//    }
//
//    public Token generateTokenCacheValid() {
//        Token token = new Token();
//        User user = new User();
//        user.setUserName(USER_HAS_CACHE_TOKEN_VALID);
//        token.setUserId(user.getId());
//        token.setToken(USER_HAS_CACHE_TOKEN_VALID);
//
//        return token;
//    }
//
//    public Token generateTokenExpire() {
//        Token token = new Token();
//        User user = new User();
//        user.setUserName(USER_HAS_CACHE_TOKEN_EXPIRE);
//        token.setUser(user);
//        token.setToken(USER_HAS_CACHE_TOKEN_EXPIRE);
//
//        return token;
//    }
//
//    public User generateUserFull() {
//        User user = new User();
//        user.setName(NAME);
//        user.setEmail(EMAIL);
//        user.setPassword(PASSWORD);
//        user.setRole(ROLE);
//        user.setUserType(USER_TYPE);
//        user.setId(ID);
//        user.setUserName(USER_NAME);
//
//        return user;
//    }
//
//    public UserDto generateUserDtoFull() {
//        UserDto user = new UserDto();
//        user.setName(NAME);
//        user.setEmail(EMAIL);
//        user.setPassword(PASSWORD);
//        user.setRole(ROLE);
//        user.setUserType(USER_TYPE);
//        user.setUserName(USER_NAME);
//
//        return user;
//    }
//
//    public User generateUserBlankPassword() {
//        User user = new User();
//        user.setPassword(BLANK_STRING);
//
//        return user;
//    }
//
//    public User generateUserShortPassword() {
//        User user = new User();
//        user.setPassword(SHORT_PASSWORD);
//
//        return user;
//    }
//
//    public UserDto generateUserForLoginExistToken() {
//        UserDto user = new UserDto();
//        user.setName(NAME);
//        user.setEmail(EMAIL);
//        user.setPassword(PASSWORD);
//        user.setRole(ROLE);
//        user.setUserType(USER_TYPE);
//        user.setUserName(USER_EXIST_TOKEN);
//
//        return user;
//    }
//
//    public UserDto generateUserForLoginNoExistToken() {
//        UserDto user = new UserDto();
//        user.setName(NAME);
//        user.setEmail(EMAIL);
//        user.setPassword(PASSWORD);
//        user.setRole(ROLE);
//        user.setUserType(USER_TYPE);
//        user.setUserName(USER_NO_EXIST_TOKEN);
//
//        return user;
//    }
//
//    public UserDetails generateUserDetails() {
//        return new org.springframework.security.core.userdetails.User(
//                USER_NAME,
//                new String(),
//                new ArrayList<>()
//        );
//    }
//
//    public Optional<Article> generateArticleForFindByUrlFriendly() {
//        Article articleDto = new Article();
//        articleDto.setTitle(ARTICLE_GET_ONE);
//
//        return Optional.of(articleDto);
//    }
//
//    public List< ArticleShortDto > generateArticleList() {
//        ArticleShortDto articleDto = new ArticleShortDto();
//        articleDto.setTitle(ARTICLE_TITLE);
//
//        return Arrays.asList(articleDto);
//    }
//}
