package com.damian.marketgrid.portal.banker

import com.damian.marketgrid.portal.RawResource
import jakarta.enterprise.context.ApplicationScoped
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory

@ApplicationScoped
class BankerScraper {
    private val logger = LoggerFactory.getLogger(BankerScraper::class.java)

    private companion object {
        const val BANKER_URL = "https://www.bankier.pl/surowce/notowania"
    }

    fun scrapeResources(): List<RawResource> {
        logger.info("Starting scraping resources from $BANKER_URL")
        var failed = 0
		return Jsoup.connect(BANKER_URL).get()
            .select("table.m-quotes-data-table tbody tr")
            .mapNotNull { row ->
                runCatching { scrapeResource(row) }
                    .onFailure { e ->
                        failed++
                        logger.error("Failed to parse table row: \"${row.text()}\" with error: ${e.message}", e)
                    }
                    .getOrNull()
            }
            .distinctBy { it.name }
            .also {
                logger.info("Finished scraping ${it.size} resources from $BANKER_URL (skipped $failed invalid rows)")
            }
    }

    internal fun fetchDocument(): Document {
		return Jsoup.connect(BANKER_URL).get()
	}

    private fun scrapeResource(row: Element): RawResource {
        val currencyAndUnit = extractField(row, 9).split("/")
        return RawResource(
            name = extractField(row, 1),
            price = convertPrice(extractField(row, 2)),
            currency = currencyAndUnit[0],
            unit = currencyAndUnit[1],
            updatedAt = extractField(row, 8)
        )
    }

    private fun convertPrice(price: String): Double {
        return price
            .replace(" ", "")
            .replace(",", ".")
            .toDouble()
    }

    private fun extractField(tableRow: Element, index: Int): String {
        return tableRow.select("td:nth-child($index)").text()
    }
}
