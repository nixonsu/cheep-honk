import clients.distancematrix.GoogleDistanceMatrixService
import clients.geocode.GoogleGeocodingService
import clients.petrolspy.PetrolSpyService
import clients.telegram.TelegramClient
import com.amazonaws.services.lambda.runtime.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import service.FuelPriceService
import utils.makeNotificationMessage
import java.net.http.HttpClient

class ApplicationHandler : RequestHandler<Map<String, Any>, String> {
    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        // Initialise dependencies
        val httpClient = HttpClient.newHttpClient()
        val objectMapper: ObjectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule())
        val petrolSpyClient = PetrolSpyService(httpClient, objectMapper)
        val googleGeocodingClient = GoogleGeocodingService(httpClient, objectMapper)
        val googleDistanceMatrixClient = GoogleDistanceMatrixService(httpClient, objectMapper)
        val fuelPriceService = FuelPriceService(petrolSpyClient, googleGeocodingClient, googleDistanceMatrixClient)
        val telegramClient = TelegramClient(httpClient, objectMapper)

        val stations = fuelPriceService.getNCheapestStationsBySuburb(5, "Springvale")

        val message = makeNotificationMessage(stations)

        telegramClient.notify(message)

        return "OK"
    }
}

class CustomContext : Context {
    override fun getAwsRequestId(): String {
        TODO("Not yet implemented")
    }

    override fun getLogGroupName(): String {
        TODO("Not yet implemented")
    }

    override fun getLogStreamName(): String {
        TODO("Not yet implemented")
    }

    override fun getFunctionName(): String {
        TODO("Not yet implemented")
    }

    override fun getFunctionVersion(): String {
        TODO("Not yet implemented")
    }

    override fun getInvokedFunctionArn(): String {
        TODO("Not yet implemented")
    }

    override fun getIdentity(): CognitoIdentity {
        TODO("Not yet implemented")
    }

    override fun getClientContext(): ClientContext {
        TODO("Not yet implemented")
    }

    override fun getRemainingTimeInMillis(): Int {
        TODO("Not yet implemented")
    }

    override fun getMemoryLimitInMB(): Int {
        TODO("Not yet implemented")
    }

    override fun getLogger(): LambdaLogger {
        TODO("Not yet implemented")
    }

}

fun main() {
    val applicationHandler = ApplicationHandler()
    applicationHandler.handleRequest(emptyMap(), CustomContext())
}
