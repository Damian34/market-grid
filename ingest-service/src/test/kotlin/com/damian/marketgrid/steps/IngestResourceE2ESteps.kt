package com.damian.marketgrid.steps

import com.damian.marketgrid.messaging.producer.IngestProducer
import com.damian.marketgrid.messaging.event.CurrencyRatesIngestedEvent
import com.damian.marketgrid.messaging.event.MarketResourceIngestedEvent
import com.damian.marketgrid.model.Currency
import com.damian.marketgrid.model.MarketResource
import com.damian.marketgrid.repository.CurrencyRepository
import com.damian.marketgrid.service.CurrencyService
import com.damian.marketgrid.service.ResourceService
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.quarkiverse.cucumber.ScenarioScope
import io.quarkus.test.junit.QuarkusMock
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions

@ScenarioScope
class IngestResourceE2ESteps @Inject constructor(
    val currencyService: CurrencyService,
    val resourceService: ResourceService,
    val currencyRepository: CurrencyRepository
) {
    private lateinit var ingestProducerMock: IngestProducerMock
    private var ingestedCurrencies: List<Currency> = emptyList()
    private var ingestedResources: List<MarketResource> = emptyList()

    @Before
    fun setupMocks() {
        if (!::ingestProducerMock.isInitialized) {
            ingestProducerMock = IngestProducerMock()
            QuarkusMock.installMockForType(ingestProducerMock, IngestProducer::class.java)
        }
        ingestProducerMock.clear()
    }

    @When("the service ingested currencies")
    fun ingestCurrencies() {
        ingestedCurrencies = currencyService.ingestCurrencies()
    }

    @Then("the currencies should be sent by Kafka to {string}")
    fun checkCurrenciesSent(topic: String) {
        assert(ingestProducerMock.hasEventOfType(CurrencyRatesIngestedEvent::class.java))
        Assertions.assertTrue(ingestedCurrencies.isNotEmpty())
    }

    @Then("currencies should be stored in the database")
    fun currenciesInDatabase() {
        Assertions.assertTrue(currencyRepository.getAll().isNotEmpty())
    }

    @When("the service ingested and normalize all market resources")
    fun ingestResources() {
        ingestedResources = resourceService.ingestAllResourcesSequence().toList()
    }

    @Then("the market resources should be sent by Kafka to {string}")
    fun checkResourcesSent(topic: String) {
        assert(ingestProducerMock.hasEventOfType(MarketResourceIngestedEvent::class.java))
        Assertions.assertTrue(ingestedResources.isNotEmpty())
    }
}

