package com.damian.marketgrid.messaging.event

import com.damian.marketgrid.model.Currency

data class CurrencyRatesIngestedEvent(val currencies: List<Currency>)
