package com.damian.marketgrid.api

import com.damian.marketgrid.model.Currency
import com.damian.marketgrid.model.MarketResource
import com.damian.marketgrid.service.CurrencyService
import com.damian.marketgrid.service.ResourceService
import io.smallrye.mutiny.Multi
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam

@Path("/api/v1/ingest")
class IngestController(
    val currencyService: CurrencyService,
    val resourceService: ResourceService
) {
    @GET
    @Path("/currencies")
    fun ingestCurrencies(): List<Currency> = currencyService.ingestCurrencies()

    @GET
    @Path("/resources")
    fun streamIngestResources(): Multi<MarketResource> {
        return Multi.createFrom().iterable(resourceService.ingestAllResourcesSequence().asIterable())
    }

    @GET
    @Path("/resources/{name}")
    fun ingestResources(
        @PathParam("name") name: String
    ): List<MarketResource> = resourceService.ingestResources(name)

}

