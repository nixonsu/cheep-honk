package clients

import domain.Location
import domain.TravelInfo

interface TravelInfoService {
    fun getTravelInfoBetween(origin: Location, destination: Location): TravelInfo
}
