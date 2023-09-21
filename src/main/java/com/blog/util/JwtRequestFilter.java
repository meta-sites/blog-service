package com.blog.util;

import com.blog.repositories.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private TokenRepository tokenRepository;

    public JwtRequestFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    private List<String> noExcludedPaths = Arrays.asList("^/private/.*");

    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        final String authorizationHeader = request.getHeader("Authorization");
//        final String requestPath = request.getRequestURI();
//        String username = null;
//        String jwtToken = null;
//        Boolean isPrivateAPI = noExcludedPaths.stream().anyMatch(requestPath::matches);
//        Boolean invalidToken = Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ");
//
//        if (!isPrivateAPI) {
//            if (invalidToken) {
//                try {
//                    jwtToken = authorizationHeader.substring(7);
//                    username = JwtTokenUtil.extractUsername(jwtToken);
//                    List< SimpleGrantedAuthority > authorities = JwtTokenUtil.extractAuthorities(jwtToken).stream()
//                            .map(SimpleGrantedAuthority::new)
//                            .collect(Collectors.toList());
//                    validateTokenValid(jwtToken, username, response);
//
//                    Boolean isExistInContext = Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
//                    if (isExistInContext) {
//                        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//                } catch (Exception ex) {}
//
//            }
//            filterChain.doFilter(request, response);
//        } else
//        {
//            if (invalidToken) {
//                jwtToken = authorizationHeader.substring(7);
//                username = JwtTokenUtil.extractUsername(jwtToken);
//                List< SimpleGrantedAuthority > authorities = JwtTokenUtil.extractAuthorities(jwtToken).stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//                validateTokenValid(jwtToken, username, response);
//
//                Boolean isExistInContext = Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
//                if (isExistInContext) {
//                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//                filterChain.doFilter(request, response);
//            } else {
//                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization token not found");
//                return;
//            }
//
//        }
//   }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String requestPath = request.getRequestURI();
        String username = null;
        String jwtToken = null;
        Boolean invalidToken = false;

        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            invalidToken = true;
            jwtToken = authorizationHeader.substring(7);
            username = JwtTokenUtil.extractUsername(jwtToken);
            List<SimpleGrantedAuthority> authorities = JwtTokenUtil.extractAuthorities(jwtToken).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            validateTokenValid(jwtToken, username, response);

            Boolean isExistInContext = Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
            if (isExistInContext) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        Boolean isPrivateAPI = noExcludedPaths.stream().anyMatch(requestPath::matches);

        if (isPrivateAPI && !invalidToken) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization token not found");
            return;
        }

        filterChain.doFilter(request, response);
    }


    private void validateTokenValid(String jwtToken, String username, HttpServletResponse response) throws IOException {
        try {
            JwtTokenUtil.validateToken(jwtToken, username);
        } catch (ExpiredJwtException e) {
            tokenRepository.deleteAllByUserName(username);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token has expired");
            return;
        } catch (Exception e) {
            tokenRepository.deleteAllByUserName(username);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token");
            return;
        }
    }
}
