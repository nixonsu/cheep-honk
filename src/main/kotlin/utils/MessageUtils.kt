package utils

import domain.FuelStation
import extensions.toPrettyString

fun makeNotificationMessage(stations: List<FuelStation>): String {
    return stations.toPrettyString()
}
