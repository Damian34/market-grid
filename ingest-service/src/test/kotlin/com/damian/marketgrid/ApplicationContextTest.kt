package com.damian.marketgrid

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.Test

@QuarkusTest
@TestProfile(IngestTestProfile::class)
class ApplicationContextTest {

    @Test
    fun contextLoads() {
    }
}
