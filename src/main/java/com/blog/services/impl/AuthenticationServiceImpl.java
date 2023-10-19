package com.blog.services.impl;

import com.blog.dto.UserDto;
import com.blog.models.Article;
import com.blog.models.User;
import com.blog.repositories.UserRepository;
import com.blog.services.AuthenticationService;
import com.blog.services.CacheService;
import com.blog.util.ExceptionConstants;
import com.blog.util.MapperUtil;
import com.blog.util.UtilFunction;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.util.Pair;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private CacheService cacheService;

    public  AuthenticationServiceImpl(UserRepository userRepository, CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }

    @Override
    public User getCurrentUser() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getPrincipal().toString();
        User entity;
        String userEntityString = cacheService.getCache(userName);
        if (Objects.nonNull(userEntityString)) {
            entity = MapperUtil.mapStringToObject(userEntityString, User.class);
        } else {
            Optional< User > user = userRepository.findByUserName(userName);
            if (!user.isPresent()) throw new UsernameNotFoundException(ExceptionConstants.USER_IS_NOT_EXIST);
            entity = user.get();
            cacheService.setCache(new Pair<>(entity.getUserName(), UtilFunction.convertObjectToString(entity)));
        }

        return entity;
    }

    @Override
    public User getCurrentUserForSave() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getPrincipal().toString();
        Optional< User > user = userRepository.findByUserName(userName);
        if (!user.isPresent()) throw new UsernameNotFoundException(ExceptionConstants.USER_IS_NOT_EXIST);
        return user.get();
    };

    @Override
    public boolean isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    };

    private User removeUserSensitiveInfo(User user) {
        user.setPassword(null);
        return user;
    }

}
