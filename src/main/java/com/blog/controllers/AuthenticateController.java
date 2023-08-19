package com.blog.controllers;

import com.blog.dto.UserDto;
import com.blog.exception.UserException;
import com.blog.services.UserService;
import com.blog.util.PathUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class AuthenticateController {

    private UserService userService;

    @Autowired
    public AuthenticateController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/public/api/sign-in")
    public ResponseEntity< UserDto > signIn(@RequestBody UserDto userDto) throws UserException, JsonProcessingException {
        return ResponseEntity
                .ok()
                .body(userService.login(userDto));
    }

    @PostMapping("/public/api/sign-in-sso")
    public ResponseEntity< UserDto > signInSSO(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity
                .ok()
                .body(userService.loginSSO(userDto));
    }

    @PostMapping("/public/api/sign-up")
    public ResponseEntity< Boolean > signUp(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity
                .ok()
                .body(userService.insert(userDto));
    }
}
