package com.damian.marketgrid.messaging.event

import com.damian.marketgrid.model.MarketResource

data class MarketResourceIngestedEvent(val resources: List<MarketResource>)
