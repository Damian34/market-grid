package com.damian.marketgrid.service.provider

import com.damian.marketgrid.model.Currency

interface CurrencyProvider {
    fun fetchCurrencies(): List<Currency>
}