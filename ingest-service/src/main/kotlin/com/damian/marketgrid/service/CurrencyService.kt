package com.damian.marketgrid.service

import com.damian.marketgrid.messaging.event.CurrencyRatesIngestedEvent
import com.damian.marketgrid.messaging.producer.IngestProducer
import com.damian.marketgrid.model.Currency
import com.damian.marketgrid.repository.CurrencyRepository
import com.damian.marketgrid.service.provider.CurrencyProvider
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.slf4j.LoggerFactory

@ApplicationScoped
class CurrencyService @Inject constructor(
    private val providers: CurrencyProvider,
    private val currencyRepository: CurrencyRepository,
    private val ingestProducer: IngestProducer
) {
    private val logger = LoggerFactory.getLogger(CurrencyService::class.java)

    fun ingestCurrencies(): List<Currency> {
        logger.info("Start currencies ingesting")
        val currencies = providers.fetchCurrencies()
        if (currencies.isEmpty()) {
            logger.info("No currencies found after ingesting")
        } else {
            currencyRepository.saveOrUpdateAll(currencies)
            ingestProducer.produce(CurrencyRatesIngestedEvent(currencies))
            logger.info("Ingested ${currencies.size} currencies")
        }
        return currencies
    }
}
