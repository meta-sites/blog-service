//package com.blog.service;
//
//import com.blog.BaseUnitTest;
//import com.blog.dto.UserDto;
//import com.blog.exception.DataBaseException;
//import com.blog.exception.PasswordDecodeException;
//import com.blog.exception.UserException;
//import com.blog.models.User;
//import com.blog.repositories.TokenRepository;
//import com.blog.repositories.UserRepository;
//import com.blog.services.CacheService;
//import com.blog.services.UserService;
//import com.blog.services.impl.CacheServiceImpl;
//import com.blog.services.impl.UserServiceImpl;
//import com.blog.util.JwtTokenUtil;
//import com.blog.util.MapperUtil;
//import com.blog.util.PasswordUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.test.context.event.annotation.BeforeTestClass;
//
//import java.util.Objects;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.mockStatic;
//import static org.springframework.test.util.AssertionErrors.assertTrue;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//public class UserServiceTest extends BaseUnitTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserDetailsService userDetailsService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private TokenRepository tokenRepository;
//
//    @Mock
//    private CacheService cacheService;
//
//    private UserDto userDto;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//
//    @BeforeAll
//    public static void setUp() {
//        Mockito.mockStatic(JwtTokenUtil.class);
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        Mockito.clearAllCaches();
//    }
//
//    @Test
//    void test_insert__empty_password_fail() {
//        assertThrows(PasswordDecodeException.class, () -> userService.validateUserEntity(generateUserBlankPassword()));
//    }
//
//    @Test
//    void test_insert__short_password_fail() {
//        assertThrows(PasswordDecodeException.class, () -> userService.validateUserEntity(generateUserShortPassword()));
//    }
//
//    @Test
//    void test_insert_exist_fail() throws DataBaseException, JsonProcessingException {
//        Mockito.when(userService.findUserByUserName(USER_NAME)).thenReturn(Optional.of(generateUserFull()));
//        assertThrows(DataBaseException.class, () -> userService.validateUserEntity(generateUserFull()));
//    }
//
//    @Test
//    void test_login_token_exist() throws JsonProcessingException, UserException {
//        Mockito.when(cacheService.getCache(USER_EXIST_TOKEN)).thenReturn(USER_EXIST_TOKEN);
//        Mockito.when(JwtTokenUtil.validateToken(USER_EXIST_TOKEN, USER_EXIST_TOKEN)).thenReturn(true);
//        Mockito.when(userService.checkTokenValid(USER_EXIST_TOKEN, USER_EXIST_TOKEN)).thenReturn(true);
//        Mockito.when(userRepository.findByUserName(USER_EXIST_TOKEN)).thenReturn(Optional.of(generateUserFull()));
//        UserDto dto = userService.login(generateUserForLoginExistToken());
//        assertEquals(USER_EXIST_TOKEN, dto.getToken());
//    }
//
//    @Test
//    void test_login_token_no_exist() throws JsonProcessingException, UserException {
//        Mockito.when(JwtTokenUtil.validateToken(USER_NO_EXIST_TOKEN, USER_NO_EXIST_TOKEN)).thenReturn(false);
//        Mockito.when(userRepository.findByUserName(USER_NO_EXIST_TOKEN)).thenReturn(Optional.of(generateUserFull()));
//        Mockito.when(userDetailsService.loadUserByUsername(USER_NO_EXIST_TOKEN)).thenReturn(generateUserDetails());
//        Mockito.when(JwtTokenUtil.generateToken(Mockito.any())).thenReturn(TOKEN);
//        UserDto dto = userService.login(generateUserForLoginNoExistToken());
//        assertEquals(dto.getToken(), TOKEN);
//    }
//
//    @Test
//    void test_checkTokenValid_has_cache_expire() {
//        Mockito.when(JwtTokenUtil.validateToken(USER_HAS_CACHE_TOKEN_EXPIRE, USER_HAS_CACHE_TOKEN_EXPIRE)).thenReturn(false);
//        Mockito.when(tokenRepository.findByUserName(USER_HAS_CACHE_TOKEN_EXPIRE)).thenReturn(Optional.of(generateTokenExpire()));
//        assertEquals(false, userService.checkTokenValid(USER_HAS_CACHE_TOKEN_EXPIRE, USER_HAS_CACHE_TOKEN_EXPIRE));
//    }
//
//    @Test
//    void test_checkTokenValid_has_cache_valid() {
//        Mockito.when(JwtTokenUtil.validateToken(USER_HAS_CACHE_TOKEN_VALID, USER_HAS_CACHE_TOKEN_VALID)).thenReturn(true);
//        assertEquals(true, userService.checkTokenValid(USER_HAS_CACHE_TOKEN_VALID, USER_HAS_CACHE_TOKEN_VALID));
//    }
//
//    @Test
//    void test_checkTokenValid_has_no_cache_valid_cache_in_db() {
//        Mockito.when(JwtTokenUtil.validateToken(USER_HAS_CACHE_TOKEN_VALID, USER_HAS_CACHE_TOKEN_EXPIRE)).thenReturn(false);
//        Mockito.when(tokenRepository.findByUserName(USER_HAS_CACHE_TOKEN_VALID)).thenReturn(Optional.of(generateTokenCacheValid()));
//        Mockito.when(JwtTokenUtil.validateToken(USER_HAS_CACHE_TOKEN_VALID, USER_HAS_CACHE_TOKEN_VALID)).thenReturn(true);
//        assertEquals(true, userService.checkTokenValid(USER_HAS_CACHE_TOKEN_VALID, USER_HAS_CACHE_TOKEN_EXPIRE));
//    }
//}
