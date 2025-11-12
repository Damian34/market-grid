package com.damian.marketgrid.auth.service;

import com.damian.marketgrid.auth.properties.AuthProperties;
import com.damian.marketgrid.user.dto.UserSession;
import com.damian.marketgrid.user.service.UserAuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final static String REDIRECT_PARAM_NAME = "redirect";
    private final OAuth2UserInfoExtractor userInfoExtractor;
    private final AuthProperties authProperties;
    private final UserAuthService userAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        UserSession userSession = userInfoExtractor.extractUserSession(oauthToken);
        log.info("Login successful for user with provider: {} and externalId: {}", userSession.provider(), userSession.externalId());

        userAuthService.createIfNotExists(userSession);
        response.sendRedirect(resolveRedirectUrl(request));
    }

    /**
     * path eg. <a href="http://localhost:8080/auth/login?redirect=/notification">...</a>
     */
    private String resolveRedirectUrl(HttpServletRequest request) {
        String redirect = request.getParameter(REDIRECT_PARAM_NAME);
        if (Strings.isNotBlank(redirect) && redirect.startsWith("/")) {
            return authProperties.getBaseUrl() + redirect;
        }
        return authProperties.getBaseUrl();
    }
}
