package com.damian.marketgrid.auth.service;


import com.damian.marketgrid.auth.properties.AuthProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private AuthProperties authProperties;

    @Mock
    private OAuth2AuthenticationToken oauth;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Test
    void shouldGiveRedirectToBaseUrlWhenAuthenticated() {
        // given
        String baseUrl = "http://localhost:8080/home";
        when(oauth.isAuthenticated()).thenReturn(true);
        when(authProperties.getBaseUrl()).thenReturn(baseUrl);

        // when
        String result = loginService.redirectLoginPage(oauth);

        // then
        assertThat(result).isEqualTo("redirect:" + baseUrl);
    }

    @Test
    void shouldGiveLoginPageWhenNotAuthenticated() {
        // given
        when(oauth.isAuthenticated()).thenReturn(false);

        // when
        String result = loginService.redirectLoginPage(oauth);

        // then
        assertThat(result).isEqualTo("login/login.html");
    }

    @Test
    void shouldInvalidateSessionAndClearCookies() {
        // given
        Cookie[] cookies = {
                new Cookie("session", "abc1"),
                new Cookie("token", "abc2")
        };
        when(request.getSession(false)).thenReturn(session);
        when(request.getCookies()).thenReturn(cookies);

        // when
        loginService.removeSession(request, response);

        // then
        verify(session).invalidate();
        for (Cookie cookie : cookies) {
            assertThat(cookie.getMaxAge()).isEqualTo(0);
            assertThat(cookie.getPath()).isEqualTo("/");
        }
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldHandleNoSessionAndNoCookiesGracefully() {
        // given
        when(request.getSession(false)).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        // when
        loginService.removeSession(request, response);

        // then
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(session, never()).invalidate();
        verify(response, never()).addCookie(any());
    }
}

