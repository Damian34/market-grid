package com.damian.marketgrid.exception

import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.slf4j.LoggerFactory

@Provider
class GlobalExceptionMapper: ExceptionMapper<GlobalException> {
    private val logger = LoggerFactory.getLogger(GlobalExceptionMapper::class.java)

    override fun toResponse(exception: GlobalException): Response {
        logger.error("GlobalException occurred: ${exception.message}", exception)
        val error = mapOf("error" to (exception.message ?: "Unknown error"))
        return Response.status(exception.code)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build()
    }
}