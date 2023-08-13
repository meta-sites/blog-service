package com.blog.services.impl;

import com.blog.exception.UserException;
import com.blog.models.User;
import com.blog.repositories.UserRepository;
import com.blog.services.CacheService;
import com.blog.services.UserService;
import com.blog.util.ExceptionConstants;
import com.blog.util.MapperUtil;
import com.blog.util.PasswordUtil;
import com.blog.util.UtilFunction;
import javafx.util.Pair;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private CacheService cacheService;

    public UserDetailsServiceImpl(UserRepository userRepository, CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        String userEntityString = cacheService.getCache(username);
        if (Objects.nonNull(userEntityString)) {
            user = MapperUtil.mapStringToObject(userEntityString, User.class);
        } else {
            Optional<User> userOptional = userRepository.findByUserName(username);
            Boolean isExist = userOptional.isPresent();
            if (!isExist) throw new UsernameNotFoundException(ExceptionConstants.USER_IS_NOT_EXIST);
            user = userOptional.get();
            cacheService.setCache(new Pair<>(user.getUserName(), UtilFunction.convertObjectToString(user)));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString())));
    }
}
