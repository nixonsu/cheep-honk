package com.nixonsu.cheephonk

import com.nixonsu.cheephonk.clients.google.GoogleDistanceMatrixClient
import com.nixonsu.cheephonk.clients.google.GoogleGeocodingClient
import com.nixonsu.cheephonk.clients.petrolspy.PetrolSpyClient
import com.nixonsu.cheephonk.clients.telegram.TelegramClient
import com.amazonaws.services.lambda.runtime.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nixonsu.cheephonk.service.FuelPriceService
import com.nixonsu.cheephonk.utils.makeNotificationMessage
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

    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        val stations = fuelPriceService.getNCheapestStationsBySuburb(5, "Springvale")

        val message = makeNotificationMessage(stations)

        telegramClient.notify(message)

        return "OK"
    }
}
