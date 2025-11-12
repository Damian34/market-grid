package com.damian.marketgrid.service

import com.damian.marketgrid.IngestTestProfile
import io.quarkiverse.cucumber.CucumberOptions
import io.quarkiverse.cucumber.CucumberQuarkusTest
import io.quarkus.test.junit.TestProfile

@CucumberOptions(
    features = ["classpath:features"],
    glue = ["com.damian.marketgrid.steps"],
    plugin = ["pretty", "summary"]
)
@TestProfile(IngestTestProfile::class)
class QuarkusCucumberTest : CucumberQuarkusTest()
