package clients

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GeocodeClient(private val httpClient: HttpClient) {

    fun getCoordinateBoundsByAddress(address: String): GeocodeResponse {
        val uri = URI.create("${GOOGLE_GEOCODE_API_URL}?address=$address&key=$API_KEY")
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return objectMapper.readValue<GeocodeResponse>(response.body())
    }

    companion object {
        private const val GOOGLE_GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json"
        private val API_KEY = System.getenv("API_KEY")
        val objectMapper: ObjectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule())
    }
}
