package com.blog.services;

import com.blog.dto.UserDto;
import com.blog.exception.TokenException;
import com.blog.exception.UserException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {

    Boolean insert(UserDto dto) throws Exception;

    UserDto login(UserDto dto) throws UserException, JsonProcessingException;

    UserDto update(UserDto dto) throws Exception;

    void logout(UserDto dto);

    UserDto requestUserInfo(UserDto dto) throws TokenException, UserException, JsonProcessingException;
}
