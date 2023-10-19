package com.blog.services;

import com.blog.dto.UserDto;
import com.blog.exception.PasswordDecodeException;
import com.blog.exception.TokenException;
import com.blog.exception.UserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    Boolean insert(UserDto dto) throws Exception;

    UserDto login(UserDto dto) throws UserException, JsonProcessingException;

    UserDto loginSSO(UserDto dto) throws Exception;

    UserDto update(UserDto dto) throws Exception;

    void logout(UserDto dto);

    UserDto requestUserInfo(UserDto dto) throws TokenException, UserException, JsonProcessingException;

    UserDto updateUserLogo(MultipartFile file) throws IOException;

    UserDto updatePathUser(UserDto userDto) throws PasswordDecodeException;
}
