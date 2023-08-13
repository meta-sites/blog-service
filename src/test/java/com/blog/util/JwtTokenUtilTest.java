//package com.blog.util;
//
//import com.blog.BaseUnitTest;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class JwtTokenUtilTest extends BaseUnitTest {
//
//    @Test
//    void test_generateToken() {
//        UserDetails userDetails = generateUserDetails();
//        String token = JwtTokenUtil.generateToken(userDetails);
//
//        assertEquals(true, token.length() > 0);
//    }
//
//    @Test
//    void test_extractUsername() {
//        UserDetails userDetails = generateUserDetails();
//        String token = JwtTokenUtil.generateToken(userDetails);
//
//        assertEquals(USER_NAME, JwtTokenUtil.extractUsername(token));
//    }
//}
