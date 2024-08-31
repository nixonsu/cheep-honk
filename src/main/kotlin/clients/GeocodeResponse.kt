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

    val bounds =  Bounds(
        northeast = Coordinates(
            lat = location.lat + 0.04,
            lng = location.lng + 0.04,
        ),
        southwest = Coordinates(
            lat = location.lat - 0.04,
            lng = location.lng - 0.04
        )
    )

    return bounds
}
