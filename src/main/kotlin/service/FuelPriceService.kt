package service

import clients.GeocodeClient
import clients.PetrolSpyClient
import clients.Station
import clients.toBounds
import extensions.toPrettyString

class FuelPriceService(private val petrolSpyClient: PetrolSpyClient, private val geocodeClient: GeocodeClient) {
    fun getNCheapestStationsBySuburb(n: Int, suburb: String): List<Station> {
        val geocodeResponse = geocodeClient.getCoordinateBoundsByAddress(suburb)

        if (geocodeResponse.results.isEmpty()) {
            return emptyList()
        }

        val bounds = geocodeResponse.toBounds()

        val petrolSpyResponse = petrolSpyClient.getStationsWithinCoordinates(bounds)

        println(petrolSpyResponse.toPrettyString())

        val stationsSortedByPrice = petrolSpyResponse.message.list.sortedBy { it.prices.u91.amount }

        return stationsSortedByPrice.take(n)
    }
}
