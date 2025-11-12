package com.damian.marketgrid.api

import com.damian.marketgrid.model.MarketResource
import com.damian.marketgrid.service.CurrencyService
import com.damian.marketgrid.service.ResourceService
import io.smallrye.mutiny.Multi
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.Response

@Path("/api/v1/ingest")
class IngestController(
    val currencyService: CurrencyService,
    val resourceService: ResourceService
) {
    @GET
    @Path("/currencies")
    fun ingestCurrencies(): Response {
        val currencies = currencyService.ingestCurrencies()
        return Response.ok(currencies).build()
    }

    @GET
    @Path("/resources")
    fun streamIngestResources(): Multi<MarketResource> {
        return Multi.createFrom().iterable(resourceService.ingestAllResourcesSequence().asIterable())
    }

    @GET
    @Path("/resources/{name}")
    fun ingestResources(@PathParam("name") name: String): Response {
        val resources = resourceService.ingestResources(name)
        return Response.ok(resources).build()
    }

}

