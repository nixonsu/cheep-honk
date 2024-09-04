package com.nixonsu.cheephonk.domain

data class Bounds(
    val northeast: Location,
    val southwest: Location
) {
    companion object {
        fun createBoundsFrom(location: Location, delta: Double): Bounds {
            return Bounds(
                northeast = Location(
                    lat = location.lat + delta,
                    lng = location.lng + delta,
                ),
                southwest = Location(
                    lat = location.lat - delta,
                    lng = location.lng - delta
                )
            )
        }
    }
}


