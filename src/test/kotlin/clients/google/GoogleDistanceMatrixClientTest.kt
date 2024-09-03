package clients.google

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import domain.Location
import exceptions.TravelInfoNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.random.Random.Default.nextDouble

class GoogleDistanceMatrixClientTest {

    private val httpClient = mockk<HttpClient>()
    private val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule())
    private val subject = GoogleDistanceMatrixClient(httpClient, objectMapper)

    @Nested
    inner class GetTravelInfoBetweenLocations {

        @Test
        fun `when travel info found should return travel distance and duration`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/distance-matrix-api-success-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse
            val randomLocation = Location(nextDouble(), nextDouble())

            val travelInfo = subject.getTravelInfoBetween(randomLocation, randomLocation)

            assertEquals(799, travelInfo.distanceInKms)
            assertEquals(99, travelInfo.durationInSeconds)
        }

        @Test
        fun `when travel info not found should throw exception`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/distance-matrix-api-not-found-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse
            val randomLocation = Location(nextDouble(), nextDouble())

            val exception = assertThrows<TravelInfoNotFoundException> {
                subject.getTravelInfoBetween(randomLocation, randomLocation)
            }

            assertEquals("Travel info not found between origin: $randomLocation, destination: $randomLocation", exception.message)
        }
    }
}
