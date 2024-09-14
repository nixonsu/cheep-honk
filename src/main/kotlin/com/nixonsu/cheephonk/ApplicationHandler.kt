package com.nixonsu.cheephonk

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nixonsu.cheephonk.clients.google.GoogleDistanceMatrixClient
import com.nixonsu.cheephonk.clients.google.GoogleGeocodingClient
import com.nixonsu.cheephonk.clients.petrolspy.PetrolSpyClient
import com.nixonsu.cheephonk.clients.telegram.TelegramClient
import com.nixonsu.cheephonk.domain.Location
import com.nixonsu.cheephonk.service.FuelPriceService
import com.nixonsu.cheephonk.utils.MaskingLogger
import com.nixonsu.cheephonk.utils.makeMarkdownMessage
import java.net.http.HttpClient

class ApplicationHandler : RequestHandler<Map<String, Any>, String> {
    private val httpClient = HttpClient.newHttpClient()
    private val objectMapper: ObjectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule())
    private val petrolSpyClient = PetrolSpyClient(httpClient, objectMapper)
    private val googleGeocodingClient = GoogleGeocodingClient(httpClient, objectMapper)
    private val googleDistanceMatrixClient = GoogleDistanceMatrixClient(httpClient, objectMapper)
    private val fuelPriceService = FuelPriceService(petrolSpyClient, googleGeocodingClient, googleDistanceMatrixClient)
    private val telegramClient = TelegramClient(httpClient, objectMapper)
    private val log = MaskingLogger.getLogger(this::class.java)

    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        val stations = fuelPriceService.getNCheapestStationsNearAddress(5, ORIGIN_ADDRESS)

        log.info("Retrieved the following stations: $stations")

        val message = makeMarkdownMessage(stations)

        log.info(message)

        telegramClient.notify(message)

        return "OK"
    }

    companion object {
        private val ORIGIN_ADDRESS = System.getenv("ORIGIN_ADDRESS")
    }
}
