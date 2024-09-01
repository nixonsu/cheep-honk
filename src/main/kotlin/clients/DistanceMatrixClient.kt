package clients

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import domain.Location
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class DistanceMatrixClient(private val httpClient: HttpClient) {

    fun getDistanceMatrix(origin: Location, destination: Location): DistanceMatrixResponse {
        val url = "$GOOGLE_DISTANCE_MATRIX_API_URL?units=metric&origins=${origin.lat},${origin.lng}&destinations=${destination.lat},${destination.lng}&key=$API_KEY"
        val uri = URI.create(url)
        println(uri)
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return objectMapper.readValue<DistanceMatrixResponse>(response.body())
    }

    companion object {
        private const val GOOGLE_DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json"
        private val API_KEY = System.getenv("GOOGLE_API_KEY")
        val objectMapper: ObjectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule())
    }
}
