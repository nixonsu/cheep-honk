package clients.geocode

import domain.Location

data class GeocodeResponse(
    val results: List<Result>,
    val errors: String,
    val status: Status
)

data class Result(
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

enum class Status {
    OK,
    REQUEST_DENIED
}
