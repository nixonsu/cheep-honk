package com.nixonsu.cheephonk.clients.google

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.cheephonk.clients.GeocodingService
import com.nixonsu.cheephonk.domain.Location
import com.nixonsu.cheephonk.utils.MaskingLogger
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GoogleGeocodingClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
) : GeocodingService {
    private val log = MaskingLogger.getLogger(this::class.java)

    override fun getLocationFor(address: String): Location? {
        val encodedAddress = URLEncoder.encode(address, Charsets.UTF_8)
        val uri = URI.create("$GOOGLE_GEOCODE_API_URL?address=$encodedAddress&key=$apiKey")
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        log.info("Send request: $request")
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val response = objectMapper.readValue<GeocodeResponse>(httpResponse.body())
        log.info("Response: $response")

        if (response.results.isEmpty()) return null

        return response.results.first().geometry.location
    }

    companion object {
        private const val GOOGLE_GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json"
        private val apiKey = System.getenv("GOOGLE_API_KEY")
    }
}
