package com.nixonsu.cheephonk.utils

import com.nixonsu.cheephonk.domain.FuelStation

fun makeNotificationMessage(stations: List<FuelStation>): String {
    val messageBuilder = StringBuilder()
    messageBuilder.append("⛽️ Cheapest 5 Fuel Stations for U91:\n\n")

    stations.forEachIndexed { index, station ->
        messageBuilder.append("${index + 1}. ${station.name} (${station.brand})\n")
        messageBuilder.append("   📍 ${station.suburb}, ${station.state}\n")
        messageBuilder.append("   🏠 ${station.address}, ${station.postcode}\n")
        messageBuilder.append("   💵 U91: ${"%.2f".format(station.prices.u91.amount)}\n")
        messageBuilder.append("   🚗 Travel Info: ${station.travelInfo.distanceInKms} km, ${station.travelInfo.durationInSeconds} s\n")
        messageBuilder.append("\n")
    }

    return messageBuilder.toString()
}
