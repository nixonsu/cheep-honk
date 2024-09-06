package com.nixonsu.cheephonk.clients.petrolspy

import com.nixonsu.cheephonk.clients.FuelStationsService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.cheephonk.domain.Bounds
import com.nixonsu.cheephonk.domain.FuelStation
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

class PetrolSpyClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
): FuelStationsService {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getStationsWithinBounds(bounds: Bounds): List<FuelStation> {
        val uri = createUri(bounds)
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        log.info("Send request: $request")
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val response = objectMapper.readValue<PetrolSpyResponse>(httpResponse.body())
        log.info("Response: $response")

        return response.message.list?.map {
            it.toFuelStation()
        } ?: emptyList()
    }

    private fun createUri(bounds: Bounds): URI? {
        val neLat = bounds.northeast.lat
        val neLng = bounds.northeast.lng
        val swLat = bounds.southwest.lat
        val swLng = bounds.southwest.lng
        val petrolSpyUrl = "$PETROL_SPY_API_URL?neLat=${neLat}&neLng=${neLng}&swLat=${swLat}&swLng=${swLng}"
        val proxyUrl = "https://app.scrapingbee.com/api/v1/?api_key=$PROXY_API_KEY&url=${
            URLEncoder.encode(
                petrolSpyUrl,
                StandardCharsets.UTF_8.toString()
            )
        }&render_js=false"
        return URI.create(proxyUrl)
    }

    companion object {
        private const val PETROL_SPY_API_URL = "https://petrolspy.com.au/webservice-1/station/box"
        private val PROXY_API_KEY = System.getenv("PROXY_API_KEY")
    }
}
