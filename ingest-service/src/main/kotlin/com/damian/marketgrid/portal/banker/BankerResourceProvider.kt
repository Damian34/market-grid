package com.damian.marketgrid.portal.banker

import com.damian.marketgrid.model.MarketResource
import com.damian.marketgrid.portal.ProviderType
import com.damian.marketgrid.service.provider.ResourceProvider
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class BankerResourceProvider @Inject constructor(
    private val bankerScraper: BankerScraper,
    private val bankerNormalizer: BankerNormalizer
): ResourceProvider {

    override fun fetchResources(): List<MarketResource> {
        val rawResources = bankerScraper.scrapeResources()
        return bankerNormalizer.normalize(rawResources)
    }

    override fun providerType(): ProviderType = ProviderType.BANKER
}
