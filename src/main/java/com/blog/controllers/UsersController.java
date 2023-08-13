package com.blog.controllers;
import com.blog.dto.UserDto;
import com.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/api/user")
public class UsersController {

    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity
                .ok()
                .body(userService.update(userDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody UserDto userDto) {
        userService.logout(userDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/request-user-info")
    public ResponseEntity< UserDto > requestUserInfo(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity
                .ok()
                .body(userService.requestUserInfo(userDto));
    }
}
