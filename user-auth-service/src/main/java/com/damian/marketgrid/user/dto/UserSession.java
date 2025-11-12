package com.damian.marketgrid.user.dto;

public record UserSession(
        String provider,
        String externalId,
        String name,
        String familyName,
        String email
) {
}
