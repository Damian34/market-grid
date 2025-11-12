package com.damian.marketgrid.portal.banker

import com.damian.marketgrid.IngestTestProfile
import com.damian.marketgrid.model.Currency
import com.damian.marketgrid.portal.RawResource
import com.damian.marketgrid.repository.CurrencyRepository
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import jakarta.inject.Inject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.collections.forEach

@QuarkusTest
@TestProfile(IngestTestProfile::class)
class BankerResourceProviderTest {

    @Inject
    lateinit var currencyRepository: CurrencyRepository

    @Inject
    lateinit var normalizer: BankerNormalizer

    lateinit var scraper: BankerScraper

    @BeforeEach
    fun setup() {
        currencyRepository.saveOrUpdateAll(
            listOf(
                Currency("USD", "dolar amerykański", 3.6610, LocalDateTime.now()),
                Currency("EUR", "euro",4.2351, LocalDateTime.now()),
                Currency("CAD", "dolar kanadyjski",2.6128, LocalDateTime.now()),
                Currency("GBP", "funt szterling",4.8230, LocalDateTime.now()),
                Currency("JPY", "jen (Japonia)",0.023747, LocalDateTime.now()),
                Currency("MYR", "ringgit (Malezja)",0.8800, LocalDateTime.now())
            )
        )
        scraper = object : BankerScraper() {
            override fun fetchDocument(): Document {
                val resourcePath = "/portal/banker/banker_surowce_notowania.html"
                val html = this::class.java.getResourceAsStream(resourcePath) ?: error("Resource not found: $resourcePath")
                html.use {
                    return Jsoup.parse(it, "UTF-8", "")
                }
            }
        }
    }

    @Test
    fun `should scrape and normalize resources`() {
        // given
        val provider = BankerResourceProvider(scraper, normalizer)

        //when
        val resources = provider.fetchResources()

        // then
        assertTrue { resources.isNotEmpty() }
        resources.forEach { r ->
            assertTrue { r.source.isNotBlank() }
            assertTrue { r.nameEng.isNotBlank() }
            assertTrue { r.namePl.isNotBlank() }
            assertTrue { r.price > 0 }
            assertTrue { r.currency == "PLN" }
            assertTrue { r.unit != null && r.unit.isNotBlank() }
        }
    }

    @Test
    fun `should scrape resources from banker page`() {
        // when
        val resources: List<RawResource> = scraper.scrapeResources()

        // then
        assertTrue { resources.isNotEmpty() }
        resources.forEach { r ->
            assertTrue { r.name.isNotBlank() }
            assertTrue { r.price > 0 }
            assertTrue { r.currency.isNotBlank() }
            assertTrue { r.unit != null && r.unit.isNotBlank() }
            assertTrue { r.updatedAt.isNotBlank() }
        }
    }
}
