package com.damian.marketgrid.scheduler

import com.damian.marketgrid.service.CurrencyService
import com.damian.marketgrid.service.ResourceService
import io.quarkus.arc.profile.IfBuildProfile
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@IfBuildProfile("!test")
@ApplicationScoped
class IngestScheduler@Inject constructor(
    private val currencyService: CurrencyService,
    private val resourceService: ResourceService
) {

    @Scheduled(every = "5m")
    fun ingestCurrencies() {
        currencyService.ingestCurrencies()
    }

    @Scheduled(every = "5m")
    fun ingestResources() {
        resourceService.ingestAllResources()
    }
}

