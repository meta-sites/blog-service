package com.blog.services.impl;

import com.blog.dto.PdfFileDto;
import com.blog.dto.UserDto;
import com.blog.exception.*;
import com.blog.models.Token;
import com.blog.models.User;
import com.blog.repositories.TokenRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.CacheService;
import com.blog.services.PdfService;
import com.blog.services.UserService;
import com.blog.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private TokenRepository tokenRepository;
    private CacheService cacheService;
    private PdfService pdfService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDetailsService userDetailsService,
                           AuthenticationManager authenticationManager, TokenRepository tokenRepository,
                           CacheService cacheService, PdfService pdfService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.cacheService = cacheService;
        this.pdfService = pdfService;
    }

    @Override
    @Transactional
    public Boolean insert(UserDto userDto) throws Exception {
        User user = MapperUtil.map(userDto, User.class);
        validateUserEntity(user);
        setDataForUserEntity(user);


        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString())));
        User entity = userRepository.save(user);
        cacheService.setCache(new Pair<>(entity.getUserName(), UtilFunction.convertObjectToString(entity)));
        return Boolean.TRUE;
    };

    @Override
    public UserDto login(UserDto userDto) throws AuthenticationException, JsonProcessingException, UserException {
        String userName = userDto.getUserName();
        String password = userDto.getPassword();
        return getUserInfoWithoutToken(userName, password);
    }

    @Override
    @Transactional
    public void logout(UserDto userDto) {
        String userName = userDto.getUserName();
        cacheService.clearCache(userName);
        tokenRepository.deleteAllByUserName(userName);
    }

    @Override
    public UserDto requestUserInfo(UserDto dto) throws TokenException, UserException, JsonProcessingException {
        String token = dto.getToken();
        String userName = JwtTokenUtil.extractUsername(token);
        Boolean isValidToken = checkTokenValid(userName, token);
        if (!isValidToken) {
            tokenRepository.deleteAllByUserName(dto.getUserName());
            throw new TokenException(ExceptionConstants.TOKEN_IS_INVALID, HttpStatus.UNAUTHORIZED);
        }

        Optional<User> userOptional = findUserByUserName(userName);
        User user = userOptional.get();
        if (!userOptional.isPresent())
            throw new UserException(ExceptionConstants.USER_IS_NOT_EXIST, HttpStatus.NOT_FOUND);
        UserDto userDto = MapperUtil.map(user, UserDto.class);
        removeSensitiveInfo(userDto);
        setTokenUserDto(userDto, token);
        setBooksSubscribe(userDto, user.getId());
        return userDto;
    }

    private String createCacheKey(String username) {
        return new StringBuilder(CacheConstants.TOKEN_CACHE).append(username).toString();
    }

    private Optional<Token> findTokenByUserName(String userName) {
        Token token = null;
        String key = createCacheKey(userName);
        String tokenValue = cacheService.getCache(key);
        if (Objects.nonNull(token)) {
            token = new Token();
            token.setToken(tokenValue);
            return Optional.of(token);
        } else {
            Optional<Token> optional = tokenRepository.findByUserName(userName);
            if (optional.isPresent()) {
                cacheService.setCache( new Pair<>(createCacheKey(userName), optional.get().getToken()));
            }
            return optional;
        }
    }


    public Boolean checkTokenValid(String userName, String token) {
        Optional<Token> entity = findTokenByUserName(userName);
        if (!entity.isPresent()) {
            cacheService.clearCache(userName);
            return false;
        }

        Boolean sameToken = token.equals(entity.get().getToken());
        if (!sameToken) return false;

        Boolean isValidToken = Objects.nonNull(token) && JwtTokenUtil.validateToken(token, userName);
        if (isValidToken) {
            cacheService.setCache(new Pair<>(createCacheKey(userName), entity.get().getToken()));
        } else {
            cacheService.clearCache(createCacheKey(userName));
            tokenRepository.delete(entity.get());
        }

        return isValidToken;
    }

    private UserDto getUserInfoWithoutToken(String userName, String password) throws JsonProcessingException, UserException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        String token = JwtTokenUtil.generateToken(userDetails);
        Optional<User> userOptional = findUserByUserName(userName);
        if (!userOptional.isPresent())
            throw new UserException(ExceptionConstants.USER_IS_NOT_EXIST, HttpStatus.BAD_REQUEST);
        User user = userOptional.get();
        UserDto dto = MapperUtil.map(user, UserDto.class);
        removeSensitiveInfo(dto);
        setBooksSubscribe(dto, user.getId());
        setTokenUserDto(dto, token);
        saveTokenInfo(token, user);
        cacheService.setCache(new Pair<>( createCacheKey(userName), token));

        return dto;
    };

    private void setBooksSubscribe(UserDto dto, String userId) {
        List<String> books = pdfService.findScribeByUserId(userId);
        dto.setBooks(books);
    }


    private UserDto getUserInfoWithTokenValid(String userName) throws JsonProcessingException {
        String token = cacheService.getCache(createCacheKey(userName));
        User user = findUserByUserName(userName).get();
        UserDto dto = MapperUtil.map(user, UserDto.class);
        removeSensitiveInfo(dto);
        setTokenUserDto(dto, token);

        return dto;
    };

    @Override
    @Transactional
    public UserDto update(UserDto userDto) throws Exception {
        Optional<User> userOptional = userRepository.findByUserName(userDto.getUserName());
        if (!userOptional.isPresent())
            throw new UserException(ExceptionConstants.USER_IS_NOT_EXIST, HttpStatus.NOT_FOUND);

        User user = userOptional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals(user.getUserName()))
            throw new UnAuthorizeException("You dont have permission.");

        setDataToEntity(userDto, user);
        User entity = userRepository.save(user);
        cacheService.setCache(new Pair<>(entity.getUserName(), UtilFunction.convertObjectToString(entity)));

        UserDto dto = MapperUtil.map(entity, UserDto.class);
        removeSensitiveInfo(dto);
        if (Objects.nonNull(userDto.getToken())) setTokenUserDto(dto, userDto.getToken());
        return dto;
    };

    private void saveTokenInfo(String tokenValue, User user) {
        try {
            Token token = new Token();
            token.setToken(tokenValue);
            token.setUserId(user.getId());
            tokenRepository.save(token);
        } catch (Exception ex) {
            log.info("[saveTokenInfo] Token đã tồn tại");
        }

    }

    private void setDataToEntity(UserDto userDto, User user) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
    };

    private void removeSensitiveInfo(UserDto dto) {
        dto.setPassword(null);
    }

    private PdfFileDto clearBookInfo(PdfFileDto dto) {
        PdfFileDto pdfFileDto = new PdfFileDto();
        pdfFileDto.setId(dto.getId());

        return pdfFileDto;
    }

    private void setTokenUserDto(UserDto dto, String token) {
        dto.setToken(token);
    }

    private void setDataForUserEntity(User user) {
        user.setPassword(PasswordUtil.encodePassword(user.getPassword()));
    }

    public void validateUserEntity(User user) throws PasswordDecodeException, DataBaseException, JsonProcessingException {
        Boolean isValidatePassword = PasswordUtil.isValidatePassword(user.getPassword());
         if (!isValidatePassword) throw new PasswordDecodeException(ExceptionConstants.PASS_WORD_INVALID);

        validateIfExistUser(user.getUserName());
    };

    public Optional<User> findUserByUserName(String userName) throws JsonProcessingException {
        User entity = null;
        String userEntityString = cacheService.getCache(userName);

        if (Objects.nonNull(userEntityString)) {
            entity = MapperUtil.mapStringToObject(userEntityString, User.class);
        } else {
            Optional< User > optional = userRepository.findByUserName(userName);
            if (optional.isPresent()) {
                entity = optional.get();
                cacheService.setCache(new Pair<>(entity.getUserName(), UtilFunction.convertObjectToString(entity)));
            }
        }

        return Optional.ofNullable(entity);
    }

    public void validateIfExistUser(String userName) throws DataBaseException, JsonProcessingException {
        Optional< User > optional = userRepository.findByUserName(userName);
        if (optional.isPresent()) throw new DataBaseException(ExceptionConstants.USER_ALREADY_EXIST);
    }
}
