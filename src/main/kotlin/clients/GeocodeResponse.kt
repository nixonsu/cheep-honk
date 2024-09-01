package clients

data class GeocodeResponse(
    val results: List<Result>
)

data class Result(
    val geometry: Geometry
)

data class Geometry(
    val location: Coordinates
)

data class Bounds(
    val northeast: Coordinates,
    val southwest: Coordinates
)

data class Coordinates(
    val lat: Double,
    val lng: Double
)


fun GeocodeResponse.toBounds(): Bounds {
    val location = this.results.first().geometry.location

    val delta = 0.02

    val bounds =  Bounds(
        northeast = Coordinates(
            lat = location.lat + delta,
            lng = location.lng + delta,
        ),
        southwest = Coordinates(
            lat = location.lat - delta,
            lng = location.lng - delta
        )
    )

    return bounds
}
