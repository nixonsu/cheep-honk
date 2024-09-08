package com.nixonsu.cheephonk.service

import com.nixonsu.cheephonk.clients.FuelStationsService
import com.nixonsu.cheephonk.clients.GeocodingService
import com.nixonsu.cheephonk.clients.TravelInfoService
import com.nixonsu.cheephonk.domain.Bounds
import com.nixonsu.cheephonk.domain.FuelStation
import com.nixonsu.cheephonk.domain.Location

class FuelPriceService(
    private val fuelStationsService: FuelStationsService,
    private val geocodingService: GeocodingService,
    private val travelInfoService: TravelInfoService
) {
    fun getNCheapestStationsBySuburb(n: Int, suburb: String, boundaryDelta: Double = 0.02): List<FuelStation> {
        val originLocation = geocodingService.getLocationFor(suburb) ?: return emptyList()

        return getNCheapestStationsFromOriginLocation(originLocation, n, boundaryDelta)
    }

    fun getNCheapestStationsFromOriginLocation(
        originLocation: Location,
        n: Int,
        boundaryDelta: Double = 0.02
    ): List<FuelStation> {
        val bounds = Bounds.createBoundsFrom(originLocation, boundaryDelta)

        val fuelStations = fuelStationsService.getStationsWithinBounds(bounds)

        val stationsSortedByPrice = fuelStations.sortedBy { it.prices.u91.amount }

        val cheapestStations = stationsSortedByPrice.take(n)

        enrichStationsWithTravelInfo(cheapestStations, originLocation)

        return cheapestStations
    }

    private fun enrichStationsWithTravelInfo(
        cheapestStations: List<FuelStation>,
        originLocation: Location,
    ) {
        cheapestStations.forEach { station ->
            station.apply {
                travelInfo = travelInfoService.getTravelInfoBetween(
                    originLocation, station.location
                )
            }
        }
    }

}
