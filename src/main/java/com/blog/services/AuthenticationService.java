package com.blog.services;

import com.blog.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthenticationService {
    User getCurrentUser() throws JsonProcessingException;

    User getCurrentUserForSave() throws JsonProcessingException;

    boolean isLogin();
}
