package com.nixonsu.cheephonk.clients.petrolspy

import com.nixonsu.cheephonk.clients.FuelStationsService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.cheephonk.domain.Bounds
import com.nixonsu.cheephonk.domain.FuelStation
import com.nixonsu.cheephonk.utils.DnsResolutionLogger
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PetrolSpyClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
): FuelStationsService {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getStationsWithinBounds(bounds: Bounds): List<FuelStation> {
        val uri = createUri(bounds)
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .header("Accept", "application/json, text/javascript, */*; q=0.01")
            .header("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("Connection", "keep-alive")
            .header("Referer", "https://petrolspy.com.au/map/latlng")
            .header("Sec-Fetch-Dest", "empty")
            .header("Sec-Fetch-Mode", "cors")
            .header("Sec-Fetch-Site", "same-origin")
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36")
            .header("X-Requested-With", "XMLHttpRequest")
            .header("X-ps-fp", "ab6d87bbde699e3ca4070422dd8ed2b6")
            .header("sec-ch-ua", "\"Chromium\";v=\"128\", \"Not:A-Brand\";v=\"99\", \"Google Chrome\";v=\"128\"")
            .header("sec-ch-ua-mobile", "?0")
            .header("sec-ch-ua-platform", "\"macOS\"")
            .build()

        DnsResolutionLogger().logDnsResolution()
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
        return URI.create("$PETROL_SPY_API_URL?neLat=${neLat}&neLng=${neLng}&swLat=${swLat}&swLng=${swLng}")
    }

    companion object {
        private const val PETROL_SPY_API_URL = "https://petrolspy.com.au/webservice-1/station/box"
    }
}
