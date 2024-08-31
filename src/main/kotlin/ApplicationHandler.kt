import clients.GeocodeClient
import clients.PetrolSpyClient
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import extensions.toPrettyString
import service.FuelPriceService
import java.net.http.HttpClient

class ApplicationHandler : RequestHandler<Map<String, Any>, String> {
    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        TODO("Not yet implemented")
    }
}

fun main() {
    val httpClient = HttpClient.newHttpClient()

    val petrolSpyClient = PetrolSpyClient(httpClient)
    val geocodeClient = GeocodeClient(httpClient)

    val fuelPriceService = FuelPriceService(petrolSpyClient, geocodeClient)

    println(
        fuelPriceService.getNCheapestStationsBySuburb(5, "Caulfield").toPrettyString()
    )
}
