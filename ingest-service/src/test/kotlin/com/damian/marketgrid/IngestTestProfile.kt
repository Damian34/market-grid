package com.damian.marketgrid

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import io.quarkus.test.junit.QuarkusTestProfile

class IngestTestProfile : QuarkusTestProfile {

    private companion object {
        const val PROFILE = "test"
        const val PROFILE_FILE = "application-$PROFILE.yml"
    }

    override fun getConfigProfile() = PROFILE

    override fun getConfigOverrides(): Map<String, String> {
        val yaml = javaClass.classLoader.getResourceAsStream(PROFILE_FILE)
            ?: throw IllegalStateException("Cannot find $PROFILE_FILE")
        
        val mapper = ObjectMapper(YAMLFactory())
        val map = mapper.readValue<Map<String, Any>>(yaml)

        return flatten(map)
    }

    @Suppress("UNCHECKED_CAST")
    private fun flatten(map: Map<String, Any>, prefix: String = ""): Map<String, String> {
        val propMap = HashMap<String, String>()
        map.forEach { (key, value) ->
            val fullKey = if (prefix.isEmpty()) key else "$prefix.$key"
            when (value) {
                is Map<*, *> -> propMap.putAll(flatten(value as Map<String, Any>, fullKey))
                is List<*> -> propMap[fullKey] = value.joinToString(",")
                else -> propMap[fullKey] = value.toString()
            }
        }
        return propMap
    }
}
