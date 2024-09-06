package com.nixonsu.cheephonk.utils

import com.nixonsu.cheephonk.domain.FuelStation
import kotlin.math.round

fun makeU91NotificationMessage(stations: List<FuelStation>): String {
    val messageBuilder = StringBuilder()
    messageBuilder.append("⛽️ Cheapest ${stations.size} Fuel Stations for U91 💥\n\n")

    stations.forEachIndexed { index, station ->
        messageBuilder.append("${index + 1}. ${station.name} (${station.brand})\n")
        messageBuilder.append("   📍 ${station.suburb}, ${station.state}\n")
        messageBuilder.append("   🏠 ${station.address}, ${station.postcode}\n")
        messageBuilder.append("   💵 Price: ${"%.2f".format(station.prices.u91.amount)}\n")
        messageBuilder.append(
            "   🚗 Travel: ${
                "%.1f".format(station.travelInfo.distanceInMs / 1000.0).toDouble()
            } kms, ${round(station.travelInfo.durationInSeconds / 60.0).toInt()} mins\n"
        )
        messageBuilder.append("\n")
    }

    return messageBuilder.toString()
}
