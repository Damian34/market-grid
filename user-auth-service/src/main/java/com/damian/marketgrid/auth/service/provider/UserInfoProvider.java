package com.damian.marketgrid.auth.service.provider;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface UserInfoProvider {
    boolean supports(String provider);
    String extractEmail(OAuth2AuthenticationToken token);
    String extractName(OAuth2AuthenticationToken token);
    String extractFamilyName(OAuth2AuthenticationToken token);
}
