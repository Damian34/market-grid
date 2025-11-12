package com.damian.marketgrid.exception

abstract class GlobalException(
    message: String? = null
) : RuntimeException(message) {
    abstract val code: Int
}