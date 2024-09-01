package domain

import clients.Prices

data class FuelStation(
    val name: String,
    val brand: String,
    val state: String,
    val suburb: String,
    val location: Location,
    val prices: Prices,
    val travelInfo: TravelInfo
)

data class Location(
    val lat: Double,
    val lng: Double
)
