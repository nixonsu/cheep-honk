package clients.petrolspy

import com.fasterxml.jackson.annotation.JsonAlias
import domain.FuelStation
import domain.Location

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

data class Prices(
    @JsonAlias("U91")
    val u91: U91
)

data class U91(
    val amount: Double
)

fun Station.toFuelStation() = FuelStation(
    name, brand, state, suburb, Location(location.lat, location.lng), prices.toDomain()
)

fun Prices.toDomain() = domain.Prices(u91.toDomain())

fun U91.toDomain() = domain.U91(amount)
