package com.damian.marketgrid.auth.service.provider;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class GithubUserInfoProvider implements UserInfoProvider {
    @Override
    public boolean supports(String provider) {
        return "github".equalsIgnoreCase(provider);
    }

    @Override
    public String extractEmail(OAuth2AuthenticationToken token) {
        return null;
    }

    @Override
    public String extractName(OAuth2AuthenticationToken token) {
        return null;
    }

    @Override
    public String extractFamilyName(OAuth2AuthenticationToken token) {
        return null;
    }
}
