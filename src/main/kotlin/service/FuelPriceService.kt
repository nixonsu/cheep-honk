package service

import clients.*
import domain.FuelStation
import domain.Location

class FuelPriceService(
    private val petrolSpyClient: PetrolSpyClient,
    private val geocodeClient: GeocodeClient,
    private val distanceMatrixClient: DistanceMatrixClient
) {
    fun getNCheapestStationsBySuburb(n: Int, suburb: String): List<FuelStation> {
        val geocodeResponse = geocodeClient.getCoordinateBoundsByAddress(suburb)

        if (geocodeResponse.results.isEmpty()) return emptyList()

        val bounds = geocodeResponse.toBounds()

        val petrolSpyResponse = petrolSpyClient.getStationsWithinCoordinates(bounds)

        val stationsSortedByPrice = petrolSpyResponse.message.list.sortedBy { it.prices.u91.amount }

        val cheapestStations = stationsSortedByPrice.take(n)

        val enrichedStations = cheapestStations.map {
            it.toFuelStation(
                distanceMatrixClient.getDistanceMatrix(ORIGIN, it.location.toDomain()).toTravelInfo()
            )
        }

        return enrichedStations
    }

    companion object {
        private val ORIGIN = Location( -37.8770781, 145.0449557)
    }
}
