package com.nixonsu.cheephonk.clients.telegram

import com.nixonsu.cheephonk.clients.NotificationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.nixonsu.cheephonk.exceptions.AuthenticationFailedException
import com.nixonsu.cheephonk.exceptions.NotificationFailedToSendException
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

class TelegramClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
) : NotificationService {
    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        authenticate()
    }

    override fun notify(message: String) {
        val messageRequest = MessageRequest(chatId = TELEGRAM_CHAT_ID, text = message)
        println(objectMapper.writeValueAsString(messageRequest))
        val request = HttpRequest.newBuilder()
            .POST(BodyPublishers.ofString(objectMapper.writeValueAsString(messageRequest)))
            .headers("Content-Type", "application/json")
            .uri(URI.create(TELEGRAM_SEND_MESSAGE_URL))
            .build()

        log.info(request.toString())

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw NotificationFailedToSendException(
                "Failed to send message to Telegram. Response status code: ${response.statusCode()}, error: ${response.body()}"
            )
        }
    }

    private fun authenticate() {
        val request = HttpRequest.newBuilder().GET().uri(URI.create(TELEGRAM_AUTH_URL)).build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw AuthenticationFailedException("Failed to authenticate with provided Telegram auth token")
        }

        log.info("Successfully authenticated with Telegram")
    }

    companion object {
        private val TELEGRAM_AUTH_TOKEN = System.getenv("TELEGRAM_AUTH_TOKEN")
        private val TELEGRAM_CHAT_ID = System.getenv("TELEGRAM_CHAT_ID") ?: ""
        private val TELEGRAM_AUTH_URL = "https://api.telegram.org/bot$TELEGRAM_AUTH_TOKEN/getMe"
        private val TELEGRAM_SEND_MESSAGE_URL = "https://api.telegram.org/bot$TELEGRAM_AUTH_TOKEN/sendMessage"
    }
}
