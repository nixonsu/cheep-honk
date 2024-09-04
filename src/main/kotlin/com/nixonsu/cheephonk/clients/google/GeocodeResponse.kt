package com.nixonsu.cheephonk.clients.google

import com.nixonsu.cheephonk.domain.Location

data class GeocodeResponse(
    val results: List<Result>,
    val errors: String?,
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
    REQUEST_DENIED,
    ZERO_RESULTS,
    NOT_FOUND
}
