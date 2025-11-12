package com.damian.marketgrid.portal

import com.damian.marketgrid.exception.InvalidProviderException

enum class ProviderType (
    val pathName: String,
    val source: String
) {
    BANKER("bankier", "bankier.pl");

    companion object {
        fun fromPath(path: String): ProviderType =
            entries.find { it.pathName.equals(path, ignoreCase = true) }
                ?: throw InvalidProviderException("No supported source type: $path")
    }
}
