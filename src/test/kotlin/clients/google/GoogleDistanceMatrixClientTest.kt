package clients.google

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GoogleDistanceMatrixClientTest {

    private val httpClient = mockk<HttpClient>()
    private val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule())
    private val subject = GoogleDistanceMatrixClient(httpClient, objectMapper)

    @Nested
    inner class GetTravelInfoBetweenLocations {

        @Test
        fun `when address is found should return location`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/geocode-api-found-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            val location = subject.getTravelInfoBetween()

            assertEquals(-14.9230049, location?.lat)
            assertEquals( 133.0667202, location?.lng)
        }

        @Test
        fun `when address is not found should return null`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/geocode-api-not-found-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            val location = subject.getLocationFor("Mata")

            assertNull(location)
        }
    }
}
