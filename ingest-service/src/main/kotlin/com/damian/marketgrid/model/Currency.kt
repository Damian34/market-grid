package com.damian.marketgrid.model

import java.time.LocalDateTime

data class Currency(
    val code: String,
    val name: String,
    val rate: Double,
    val updatedAt: LocalDateTime,
)
