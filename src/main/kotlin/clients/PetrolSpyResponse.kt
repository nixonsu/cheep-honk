package clients

import com.fasterxml.jackson.annotation.JsonAlias
import domain.FuelStation
import domain.Location
import domain.TravelInfo

data class PetrolSpyResponse(
    val message: Message
)

data class Message(
    val list: List<Station>
)

data class Station(
    val name: String,
    val brand: String,
    val state: String,
    val suburb: String,
    val location: PetrolSpyLocation,
    val prices: Prices
)

data class PetrolSpyLocation(
    @JsonAlias("y")
    val lat: Double,
    @JsonAlias("x")
    val lng: Double
)

fun PetrolSpyLocation.toDomain() = Location(lat, lng)

data class Prices(
    @JsonAlias("U91")
    val u91: U91
)

data class U91(
    val amount: Double
)

fun Station.toFuelStation(travelInfo: TravelInfo) = FuelStation(
    name, brand, state, suburb, location.toDomain(), prices, travelInfo
)
