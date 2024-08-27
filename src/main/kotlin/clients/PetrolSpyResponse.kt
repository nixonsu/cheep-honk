package clients

import com.fasterxml.jackson.annotation.JsonAlias

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
    val location: Location,
    val prices: Prices
)

data class Location(
    val x: Double,
    val y: Double
)

data class Prices(
    @JsonAlias("U91")
    val u91: U91
)

data class U91(
    val amount: Double
)
