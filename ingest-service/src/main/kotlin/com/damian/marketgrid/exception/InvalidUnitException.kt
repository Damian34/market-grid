package com.damian.marketgrid.exception

class InvalidUnitException(message: String): GlobalException(message) {
    override val code: Int = 500
}
