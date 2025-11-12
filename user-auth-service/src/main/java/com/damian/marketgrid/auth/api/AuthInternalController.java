package com.damian.marketgrid.auth.api;

import com.damian.marketgrid.auth.service.OAuth2UserInfoExtractor;
import com.damian.marketgrid.user.dto.UserAuthDetails;
import com.damian.marketgrid.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class AuthInternalController {
    private final OAuth2UserInfoExtractor userInfoExtractor;
    private final UserAuthService userAuthService;

    @GetMapping("/user")
    public ResponseEntity<UserAuthDetails> getUserAuthDetails(OAuth2AuthenticationToken oauth) {
        String provider = userInfoExtractor.extractProvider(oauth);
        String externalId = userInfoExtractor.extractExternalId(oauth, provider);
        UserAuthDetails userAuthDetails = userAuthService.getUserAuthDetails(provider, externalId);
        return ResponseEntity.ok(userAuthDetails);
    }
}
