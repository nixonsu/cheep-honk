package com.nixonsu.cheephonk.utils

import com.nixonsu.cheephonk.domain.FuelStation
import kotlin.math.round

fun makeMarkdownMessage(stations: List<FuelStation>): String {
    val messageBuilder = StringBuilder()
    messageBuilder.append(makeHeader(stations))

    stations.forEachIndexed { index, station ->
        messageBuilder.append(makeStationDetails(index, station))
        messageBuilder.append(makeLocationDetails(station))
        messageBuilder.append(makePriceDetails(station))
        messageBuilder.append(makeTravelDetails(station))
        messageBuilder.append(makeDirectionsUrl(station))
        messageBuilder.append("\n")
    }

    return messageBuilder.toString()
}

private fun makeHeader(stations: List<FuelStation>) =
    "⛽️ *Top ${stations.size} Fuel Stations Near You* 💥\n\n"

private fun makeStationDetails(index: Int, station: FuelStation) =
    "*${index + 1}. ${station.name}* (${station.brand})\n"

private fun makeLocationDetails(station: FuelStation) = "   📍 _${station.suburb}_\n"

private fun makePriceDetails(station: FuelStation) =
    "   💵 *Price*: ${"%.2f".format(station.prices.u91.amount)}\n"

private fun makeTravelDetails(station: FuelStation) = "   🚗 *Travel*: ${
    "%.1f".format(station.travelInfo.distanceInMs / 1000.0).toDouble()
} kms, ${round(station.travelInfo.durationInSeconds / 60.0).toInt()} mins\n"

private fun makeDirectionsUrl(station: FuelStation) =
    "[Get Directions](https://www.google.com/maps/dir/?api=1&destination=${station.address.replace(" ", "+")})\n"
