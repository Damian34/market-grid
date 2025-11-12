package com.damian.marketgrid.service.provider

import com.damian.marketgrid.model.MarketResource
import com.damian.marketgrid.portal.ProviderType

interface ResourceProvider {
    fun fetchResources(): List<MarketResource>

    fun providerType(): ProviderType
}
