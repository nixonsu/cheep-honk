import clients.geocode.GoogleGeocodingClient
import clients.geocode.GeocodeResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GoogleGeocodingClientTest {

    private val httpClient = mockk<HttpClient>()
    private val subject = GoogleGeocodingClient(httpClient)

    @Nested
    inner class GetCoordinateBoundsByAddress {

        @Test
        fun `when address is found return bounds`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/geocode-api-found-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            val response = subject.getCoordinateBoundsByAddress("Mataranka")

            assertEquals(-14.9230049, response.results.first().geometry.location.lat)
            assertEquals( 133.0667202, response.results.first().geometry.location.lng)
        }

        @Test
        fun `when address is not found return empty list of results`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/geocode-api-not-found-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            assertEquals(GeocodeResponse(emptyList()), subject.getCoordinateBoundsByAddress("Marka"))
        }
    }
}
