package service

import clients.FuelStationsService
import clients.GeocodingService
import clients.TravelInfoService
import domain.Bounds
import domain.FuelStation
import domain.Location

class FuelPriceService(
    private val fuelStationsService: FuelStationsService,
    private val geocodingService: GeocodingService,
    private val travelInfoService: TravelInfoService
) {
    fun getNCheapestStationsBySuburb(n: Int, suburb: String): List<FuelStation> {
        val location = geocodingService.getLocationFor(suburb) ?: return emptyList()

        val bounds = Bounds.createBoundsFrom(location, 0.02)

        val fuelStations = fuelStationsService.getStationsWithinBounds(bounds)

        val stationsSortedByPrice = fuelStations.sortedBy { it.prices.u91.amount }

        val cheapestStations = stationsSortedByPrice.take(n)

        cheapestStations.forEach {
            it.apply {
                travelInfo = travelInfoService.getTravelInfoBetween(
                    Location(ORIGIN_LAT, ORIGIN_LNG), it.location
                )
            }
        }

        return cheapestStations
    }

    companion object {
        private val ORIGIN_LAT = System.getenv("ORIGIN_LAT").toDouble()
        private val ORIGIN_LNG = System.getenv("ORIGIN_LNG").toDouble()
    }
}
