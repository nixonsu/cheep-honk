package clients

import domain.Location

interface GeocodingService {
    fun getLocationFor(address: String): Location?
}
