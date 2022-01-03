package ru.javamentor.spring_boot_crud.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {

        List<String> authorityNames = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (authorityNames.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else if (authorityNames.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/user");
        } else {
            throw new IllegalStateException("No user with such authority");
        }
    }
}