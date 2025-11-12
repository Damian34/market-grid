package com.damian.marketgrid.auth.service;

import com.damian.marketgrid.auth.service.provider.UserInfoProvider;
import com.damian.marketgrid.exception.OAuth2UserVerificationException;
import com.damian.marketgrid.user.dto.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2UserInfoExtractor {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final List<UserInfoProvider> providers;

    public String extractProvider(OAuth2AuthenticationToken oauthToken) {
        return oauthToken.getAuthorizedClientRegistrationId();
    }

    public String extractExternalId(OAuth2AuthenticationToken oauthToken, String provider) {
        String userIdAttribute = clientRegistrationRepository.findByRegistrationId(provider)
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        var value = oauthToken.getPrincipal().getAttribute(userIdAttribute);
        if (value == null) {
            throw new OAuth2UserVerificationException();
        }
        return value.toString();
    }

    public UserSession extractUserSession(OAuth2AuthenticationToken oauthToken) {
        String provider = extractProvider(oauthToken);
        String externalId = extractExternalId(oauthToken, provider);
        var userInfoProvider = providers.stream()
                .filter(p -> p.supports(provider))
                .findFirst()
                .orElseThrow(() -> new OAuth2UserVerificationException("Not recognised provider \"" + provider + "\""));

        String name = userInfoProvider.extractName(oauthToken);
        String familyName = userInfoProvider.extractFamilyName(oauthToken);
        String email = userInfoProvider.extractEmail(oauthToken);
        return new UserSession(provider, externalId, name, familyName, email);
    }

}
