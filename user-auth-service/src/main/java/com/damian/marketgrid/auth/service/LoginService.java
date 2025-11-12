package com.damian.marketgrid.auth.service;

import com.damian.marketgrid.auth.properties.AuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthProperties authProperties;

    public String redirectLoginPage(OAuth2AuthenticationToken oauth) {
        if (oauth != null && oauth.isAuthenticated()) {
            return "redirect:" + authProperties.getBaseUrl();
        }
        return "login/login.html";
    }

    public void removeSession(HttpServletRequest request, HttpServletResponse response) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (request.getCookies() != null) {
            Arrays.stream(request.getCookies()).forEach(c -> {
                c.setMaxAge(0);
                c.setPath("/");
                response.addCookie(c);
            });
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
