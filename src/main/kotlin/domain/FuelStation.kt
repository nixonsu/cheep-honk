package domain

data class FuelStation(
    val name: String,
    val brand: String,
    val state: String,
    val suburb: String,
    val address: String,
    val postcode: String,
    val location: Location,
    val prices: Prices
) {
    lateinit var travelInfo: TravelInfo
}

data class Prices(
    val u91: U91
)

data class U91(
    val amount: Double
)

data class Location(
    val lat: Double,
    val lng: Double
)
