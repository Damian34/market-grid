package com.damian.marketgrid.model

import java.time.LocalDateTime

data class MarketResource(
    val kind: ResourceKind,
    val source: String,
    val nameEng: String,
    val namePl: String,
    val price: Double,
    val currency: String,
    val unit: String?,
    val updatedAt: LocalDateTime
)
