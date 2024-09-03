package clients.geocode

import clients.GeocodingService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import domain.Location
import exceptions.FailedToRetrieveLocationException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GoogleGeocodingService(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
) : GeocodingService {

    override fun getLocationFor(address: String): Location? {
        val uri = URI.create("$GOOGLE_GEOCODE_API_URL?address=$address&key=$apiKey")
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val response = objectMapper.readValue<GeocodeResponse>(httpResponse.body())

        if (response.status != Status.OK) {
            throw FailedToRetrieveLocationException("Failed to retrieve location: ${response.errors}")
        }

        if (response.results.isEmpty()) return null

        return response.results.first().geometry.location
    }

    companion object {
        private const val GOOGLE_GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json"
        private val apiKey = System.getenv("GOOGLE_API_KEY")
    }
}
