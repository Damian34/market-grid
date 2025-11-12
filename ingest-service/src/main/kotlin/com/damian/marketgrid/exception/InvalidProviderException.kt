package com.damian.marketgrid.exception

class InvalidProviderException(message: String) : GlobalException(message) {
    override val code: Int = 400
}
