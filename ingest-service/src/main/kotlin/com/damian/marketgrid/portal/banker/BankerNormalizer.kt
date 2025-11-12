package com.damian.marketgrid.portal.banker

import com.damian.marketgrid.exception.InvalidUnitException
import com.damian.marketgrid.model.Currency
import com.damian.marketgrid.model.MarketResource
import com.damian.marketgrid.portal.ProviderType
import com.damian.marketgrid.portal.RawResource
import com.damian.marketgrid.portal.banker.propeties.BankerProperties
import com.damian.marketgrid.repository.CurrencyRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.round

@ApplicationScoped
class BankerNormalizer @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val bankerProperties: BankerProperties
) {
    private val logger = LoggerFactory.getLogger(BankerNormalizer::class.java)

    private companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    fun normalize(rawResources: List<RawResource>): List<MarketResource> {
        val currencyRatesToPln = getPresentCurrencyRatesToPln(rawResources)
        var skipped = 0

        val normalized = rawResources.mapNotNull {
            it.validate()
            mapResource(it, currencyRatesToPln) ?: run {
                skipped++
                logger.debug("Skipped resource normalization: ${it.name}")
                null
            }
        }

        logger.info("Normalized ${normalized.size} resources (skipped $skipped rows)")
        return normalized
    }

    private fun mapResource(
        rawResource: RawResource,
        currencyRatesToPln: Map<String, Currency>
    ): MarketResource? {
        val (currencyCode, adjustedPrice) = adjustCurrency(rawResource.currency, rawResource.price)
        val currency = currencyRatesToPln[currencyCode] ?: return null
        val resource = bankerProperties.resources[rawResource.name] ?: return null
        val (unit, price) = rawResource.unit?.let { mapUnit(it, adjustedPrice) } ?: (null to adjustedPrice)

        return MarketResource(
            kind = resource.kind,
            source = ProviderType.BANKER.source,
            nameEng = resource.nameEng,
            namePl = resource.namePl,
            price = mapPrice(price, currency),
            currency = "PLN",
            unit = unit,
            updatedAt = LocalDateTime.parse(rawResource.updatedAt, formatter)
        )
    }

    private fun mapUnit(unit: String, price: Double): Pair<String, Double> {
        return bankerProperties.units.getOrElse(unit) {
            throw InvalidUnitException("No unit to map found for: \"$unit\"")
        }.let {
            Pair(it.nameNew, (1.0 / it.priceScalar) * price)
        }
    }

    private fun mapPrice(price: Double, currency: Currency): Double {
        val factor = 10.0.pow(6)
        return round(price * currency.rate * factor) / factor
    }

    private fun adjustCurrency(currency: String, price: Double): Pair<String, Double> {
        if (currency.equals("USc", ignoreCase = true)) {
            return Pair("USD", price / 100)
        }
        return Pair(currency, price)
    }

    private fun getPresentCurrencyRatesToPln(rawResources: List<RawResource>): Map<String, Currency> {
        val codes = rawResources.map { it.currency }.toMutableSet()

        if (codes.any { it.equals("USc", ignoreCase = true)}) {
            codes.remove("USc")
            codes.add("USD")
        }

        return currencyRepository.findByCodeIn(codes).associateBy { it.code }
    }
}
