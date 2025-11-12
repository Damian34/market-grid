package com.damian.marketgrid.auth.service.provider;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class GoogleUserInfoProvider implements UserInfoProvider {
    @Override
    public boolean supports(String provider) {
        return "google".equalsIgnoreCase(provider);
    }

    @Override
    public String extractEmail(OAuth2AuthenticationToken token) {
        return token.getPrincipal().getAttribute("email");
    }

    @Override
    public String extractName(OAuth2AuthenticationToken token) {
        return token.getPrincipal().getAttribute("given_name");
    }

    @Override
    public String extractFamilyName(OAuth2AuthenticationToken token) {
        return token.getPrincipal().getAttribute("family_name");
    }
}
