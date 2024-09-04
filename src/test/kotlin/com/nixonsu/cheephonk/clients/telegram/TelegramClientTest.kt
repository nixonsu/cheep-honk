package com.nixonsu.cheephonk.clients.telegram

import com.nixonsu.cheephonk.clients.telegram.TelegramClient
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nixonsu.cheephonk.exceptions.AuthenticationFailedException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class TelegramClientTest {

    private val httpClient = mockk<HttpClient>()
    private val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule())

    @Nested
    inner class Authenticate {

        @Test
        fun `when auth token invalid should throw exception`() {
            val mockResponse: HttpResponse<String> = mockk()
            every { mockResponse.body() } returns ""
            every { mockResponse.statusCode() } returns 404
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            val exception = assertThrows<AuthenticationFailedException> {
                TelegramClient(httpClient, objectMapper)
            }
            assertEquals("Failed to authenticate with provided Telegram auth token", exception.message)
        }

        @Test
        fun `when auth token valid should not throw exception`() {
            val mockResponse: HttpResponse<String> = mockk()
            every { mockResponse.body() } returns ""
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            assertDoesNotThrow {
                TelegramClient(httpClient, objectMapper)
            }
        }
    }

    @Nested
    inner class Notify {
        @Test
        fun `when chat id valid should not throw exception`() {
            val mockResponse: HttpResponse<String> = mockk()
            every { mockResponse.body() } returns ""
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse
            val subject = TelegramClient(httpClient, objectMapper)

            assertDoesNotThrow {
                subject.notify("Test")
            }
        }
    }


}
