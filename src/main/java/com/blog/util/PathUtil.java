package com.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PathUtil {
    private HttpServletRequest request;

    @Value("${user.logo.dir}")
    private String userLogoDir;

    public PathUtil(HttpServletRequest request) {
        this.request = request;
    }

    public String getBaseUrl() {
        String currentUrl = request.getRequestURL().toString();
        String endpointPath = request.getServletPath();

        return currentUrl.replace(endpointPath, "");
    }

    public String getUserLogoDir() {
        return userLogoDir;
    }
}
