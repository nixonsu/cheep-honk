package clients.petrolspy

import clients.FuelStationsService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import domain.Bounds
import domain.FuelStation
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PetrolSpyClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
): FuelStationsService {

    override fun getStationsWithinBounds(bounds: Bounds): List<FuelStation> {
        val uri = createUri(bounds)
        val request = HttpRequest.newBuilder().GET().uri(uri).build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val response = objectMapper.readValue<PetrolSpyResponse>(httpResponse.body())

        return response.message.list.map {
            it.toFuelStation()
        }
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
