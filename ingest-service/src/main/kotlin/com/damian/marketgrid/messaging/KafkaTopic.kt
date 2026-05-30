package com.damian.marketgrid.messaging

import com.damian.marketgrid.messaging.event.CurrencyRatesIngestedEvent
import com.damian.marketgrid.messaging.event.MarketResourceIngestedEvent

enum class KafkaTopic(
    val topic: String,
    val eventClass: Class<*>
) {
    MARKET_RESOURCE_INGESTED("market-resource-ingested", MarketResourceIngestedEvent::class.java),
    CURRENCY_RATES_INGESTED("currency-rates-ingested", CurrencyRatesIngestedEvent::class.java);

    companion object {
        fun of(obj: Any): KafkaTopic =
            entries.find { it.eventClass.isInstance(obj) }
                ?: throw IllegalArgumentException("No Kafka topic exists for object of type: ${obj::class.java.name}")
    }
}
