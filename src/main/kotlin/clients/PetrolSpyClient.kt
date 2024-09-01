package clients

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PetrolSpyClient(private val httpClient: HttpClient) {

    fun getStationsWithinCoordinates(
        bounds: Bounds
    ): PetrolSpyResponse {
        val neLat = bounds.northeast.lat
        val neLng = bounds.northeast.lng
        val swLat = bounds.southwest.lat
        val swLng = bounds.southwest.lng
        val uri = URI.create("${PETROL_SPY_API_URL}?neLat=${neLat}&neLng=${neLng}&swLat=${swLat}&swLng=${swLng}")
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return objectMapper.readValue<PetrolSpyResponse>(response.body())
    }

    companion object {
        private const val PETROL_SPY_API_URL = "https://petrolspy.com.au/webservice-1/station/box"
        val objectMapper: ObjectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule())
    }
}
