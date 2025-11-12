package com.damian.marketgrid.service

import com.damian.marketgrid.exception.InvalidProviderException
import com.damian.marketgrid.messaging.event.MarketResourceIngestedEvent
import com.damian.marketgrid.messaging.producer.IngestProducer
import com.damian.marketgrid.model.MarketResource
import com.damian.marketgrid.service.provider.ResourceProvider
import com.damian.marketgrid.portal.ProviderType
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject
import org.slf4j.LoggerFactory

@ApplicationScoped
class ResourceService @Inject constructor(
    providers: Instance<ResourceProvider>,
    private val ingestProducer: IngestProducer
) {
    private val logger = LoggerFactory.getLogger(ResourceService::class.java)
    private val providersMap = providers.associateBy { it.providerType() }

	fun ingestResources(name: String): List<MarketResource> {
        val type = ProviderType.fromPath(name)
        val provider = providersMap[type] ?: throw InvalidProviderException("No supported source type: $type")
        return processResourcesByProvider(provider)
    }

    fun ingestAllResourcesSequence(): Sequence<MarketResource> = sequence {
        providersMap.values.forEach { provider ->
            processResourcesByProvider(provider).forEach { yield(it) }
        }
    }

    fun ingestAllResources() {
        providersMap.values.forEach { provider ->
            try {
                processResourcesByProvider(provider)
            } catch (e: Exception) {
                logger.error("Failed to ingest data from ${provider.providerType().source}", e)
            }
        }
    }

	private fun processResourcesByProvider(provider: ResourceProvider): List<MarketResource> {
        logger.info("Start resources ingesting for ${provider.providerType().source}")
        val resources = provider.fetchResources()
        if (resources.isEmpty()) {
            logger.info("No resources found after ingesting ${provider.providerType().source}")
        } else {
            ingestProducer.produce(MarketResourceIngestedEvent(resources))
            logger.info("Ingested ${resources.size} resources from ${provider.providerType().source}")
        }
        return resources
	}
}
