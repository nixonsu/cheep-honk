package clients.distancematrix

import clients.TravelInfoService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import domain.Location
import domain.TravelInfo
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GoogleDistanceMatrixService(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
): TravelInfoService {

    override fun getTravelInfoBetween(origin: Location, destination: Location): TravelInfo {
        val uri = URI.create(buildUrl(origin, destination))
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val response = objectMapper.readValue<DistanceMatrixResponse>(httpResponse.body())
        return response.toTravelInfo()
    }

    private fun buildUrl(origin: Location, destination: Location): String =
        buildString {
            append(GOOGLE_DISTANCE_MATRIX_API_URL)
            append("?units=metric&origins=")
            append(origin.lat)
            append(",")
            append(origin.lng)
            append("&destinations=")
            append(destination.lat)
            append(",")
            append(destination.lng)
            append("&key=")
            append(apiKey)
        }

    companion object {
        private const val GOOGLE_DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json"
        private val apiKey = System.getenv("GOOGLE_API_KEY")
    }
}
