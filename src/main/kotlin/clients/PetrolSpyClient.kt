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
        neLatitude: Double,
        neLongitude: Double,
        swLatitude: Double,
        swLongitude: Double
    ): PetrolSpyResponse {
        val uri = URI.create("${PETROL_SPY_URL}?neLat=${neLatitude}&neLng=${neLongitude}&swLat=${swLatitude}&swLng=${swLongitude}")
        val request = HttpRequest
            .newBuilder()
            .GET()
            .uri(uri)
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return objectMapper.readValue<PetrolSpyResponse>(response.body())
    }

    companion object {
        private const val PETROL_SPY_URL = "https://petrolspy.com.au/webservice-1/station/box"
        val objectMapper: ObjectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule())
    }
}

fun main() {
    val client = HttpClient.newBuilder().build()

    val petrolSpyClient = PetrolSpyClient(client)

    petrolSpyClient.getStationsWithinCoordinates(
        -30.9020020087044,
        149.1089495357652,
        -30.996414716299277,
        149.02473246423102
    )
}
