package com.damian.marketgrid.portal.nbp

import com.damian.marketgrid.model.Currency
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NbpCurrencyProviderTest {

    private lateinit var provider: NbpCurrencyProvider

    @BeforeEach
    fun setup() {
        provider = NbpCurrencyProvider().apply {
            currencyClient = NbpCurrencyClientMock()
        }
    }

    @Test
    fun `should fetch NBP currencies data`() {
        // when
        val currencies: List<Currency> = provider.fetchCurrencies()

        // then
        assertTrue { currencies.isNotEmpty() }
        currencies.forEach { c ->
            assertTrue { c.code.isNotBlank() }
            assertTrue { c.name.isNotBlank() }
            assertTrue { c.rate > 0 }
        }
    }
}

