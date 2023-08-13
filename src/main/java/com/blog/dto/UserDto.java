package com.blog.dto;

import com.blog.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String userName;
    private String userType;
    private RoleEnum role;
    private String email;
    private String password;
    private String token;
    private List<String> books;
}
