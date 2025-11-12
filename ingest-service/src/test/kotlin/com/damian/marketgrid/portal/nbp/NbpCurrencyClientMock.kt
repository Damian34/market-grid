package com.damian.marketgrid.portal.nbp

import com.damian.marketgrid.portal.nbp.data.Table
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class NbpCurrencyClientMock: NbpCurrencyClient  {
    private val objectMapper = jacksonObjectMapper()

    override fun getCurrencyTable(
        type: String,
        format: String
    ): List<Table> {
        val resourcePath = "/portal/nbp/table_${type.lowercase()}.json"
        val resource = this::class.java.getResource(resourcePath) ?: error("Resource not found: $resourcePath")
        val content = resource.readText()
        return objectMapper.readValue(content)
    }
}