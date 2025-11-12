package com.damian.marketgrid.user.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserIdentity(
        @Column(nullable = false) String provider,
        @Column(nullable = false) String externalId
) {
}
