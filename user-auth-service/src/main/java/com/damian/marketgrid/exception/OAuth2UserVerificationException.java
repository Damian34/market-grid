package com.damian.marketgrid.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserVerificationException extends GlobalException {
    private String message =  "Authentication failed: cannot verify user identity.";

    @Override
    public HttpStatus getCode() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
