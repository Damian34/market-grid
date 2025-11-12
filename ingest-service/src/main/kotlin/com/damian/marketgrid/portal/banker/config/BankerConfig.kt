package com.damian.marketgrid.portal.banker.config

import com.damian.marketgrid.portal.banker.propeties.BankerProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces

@ApplicationScoped
class BankerConfig {

    @Produces
    @ApplicationScoped
    fun bankerProperties(): BankerProperties {
        val yamlMapper = ObjectMapper(YAMLFactory())
        val resourcePath = "/portals/banker.yml"
        return this::class.java.getResourceAsStream(resourcePath)?.use { input ->
            yamlMapper.readValue(input, BankerProperties::class.java)
        } ?: throw IllegalStateException("Config file $resourcePath not found in resources")
    }
}
