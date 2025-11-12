package com.damian.marketgrid.user.messaging.event;

public record UserChangedEvent(
        Long id,
        String name,
        String familyName,
        String email
) {
}
