package com.damian.marketgrid.portal.nbp

import com.damian.marketgrid.portal.nbp.data.Table
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(baseUri = "https://api.nbp.pl")
interface NbpCurrencyClient {
    /**
     * type:
     * a - most often used currencies with average values
     * b - seldom used currencies with average values
     * c - contains buy and sell values
     * */
    @GET
    @Path("/api/exchangerates/tables/{type}/last")
    @Produces(MediaType.APPLICATION_JSON)
    fun getCurrencyTable(
        @PathParam("type") type: String,
        @QueryParam("format") format: String = "json"
    ): List<Table>
}