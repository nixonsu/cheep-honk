package com.nixonsu.cheephonk.clients.petrolspy

import com.nixonsu.cheephonk.clients.petrolspy.PetrolSpyClient
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nixonsu.cheephonk.domain.Bounds
import com.nixonsu.cheephonk.domain.Location
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.random.Random.Default.nextDouble

class PetrolSpyClientTest {

    private val httpClient = mockk<HttpClient>()
    private val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule())
    private val subject = PetrolSpyClient(httpClient, objectMapper)

    @Nested
    inner class GetStationsWithinBounds {

        @Test
        fun `when multiple stations within bounds should return stations`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/petrol-spy-api-found-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse
            val bounds = Bounds.createBoundsFrom(Location(nextDouble(), nextDouble()), 0.02)

            val stations = subject.getStationsWithinBounds(bounds)

            assertEquals(2, stations.size)
            assertEquals("BP Swan Street", stations[0].name)
            assertEquals("BP", stations[0].brand)
            assertEquals("VIC", stations[0].state)
            assertEquals("Richmond", stations[0].suburb)
            assertEquals("282 Swan St", stations[0].address)
            assertEquals("3121", stations[0].postcode)
            assertEquals(Location(-37.826133, 145.000166), stations[0].location)
            assertEquals(167.9, stations[0].prices.u91.amount)
        }

        @Test
        fun `when no stations within bounds should return empty list`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/petrol-spy-api-not-found-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse
            val bounds = Bounds.createBoundsFrom(Location(nextDouble(), nextDouble()), 0.02)

            val stations = subject.getStationsWithinBounds(bounds)

            assertTrue(stations.isEmpty())
        }

        @Test
        fun `when bounds is too large should return empty list`() {
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody = this::class.java.getResource("/petrol-spy-api-bounds-too-large-response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse
            val bounds = Bounds.createBoundsFrom(Location(nextDouble(), nextDouble()), 0.02)

            val stations = subject.getStationsWithinBounds(bounds)

            assertTrue(stations.isEmpty())
        }
    }
}
