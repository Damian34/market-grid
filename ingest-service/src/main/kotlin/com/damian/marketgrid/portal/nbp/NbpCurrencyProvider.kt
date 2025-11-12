package com.damian.marketgrid.portal.nbp

import com.damian.marketgrid.model.Currency
import com.damian.marketgrid.portal.nbp.data.Table
import com.damian.marketgrid.service.provider.CurrencyProvider
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ApplicationScoped
class NbpCurrencyProvider: CurrencyProvider {

    private companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    @Inject
    @RestClient
    lateinit var currencyClient: NbpCurrencyClient

    override fun fetchCurrencies(): List<Currency> {
        val tableA = currencyClient.getCurrencyTable("a")[0]
        val tableB = currencyClient.getCurrencyTable("b")[0]

        val mappedA = map(tableA)
        val mappedB = map(tableB)

        return mergeCurrencyRates(mappedA, mappedB)
    }

    private fun map(table: Table): List<Currency> {
        val updatedAt = LocalDate.parse(table.effectiveDate, formatter).atStartOfDay()
        return table.rates.map {
            Currency(
                code = it.code,
                name = it.currency,
                rate = it.mid,
                updatedAt = updatedAt
            )
        }
    }

    private fun mergeCurrencyRates(
        listA: List<Currency>,
        listB: List<Currency>
    ): List<Currency> {
        val intersectRates = intersectUpdatedRates(listA, listB)
        val intersectCodes = intersectRates.map { it.code }.toSet()

        val onlyA = listA.filter { it.code !in intersectCodes }
        val onlyB = listB.filter { it.code !in intersectCodes }

        return buildList {
            addAll(onlyA)
            addAll(intersectRates)
            addAll(onlyB)
        }
    }

    private fun intersectUpdatedRates(
        listA: List<Currency>,
        listB: List<Currency>
    ): List<Currency> {
        val mapByCodeB = listB.associateBy { it.code }
        return listA.mapNotNull { rateA ->
            mapByCodeB[rateA.code]?.let { rateB ->
                if (rateA.updatedAt.isAfter(rateB.updatedAt)) rateA else rateB
            }
        }
    }
}