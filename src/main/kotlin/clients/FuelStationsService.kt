package clients

import domain.Bounds
import domain.FuelStation

interface FuelStationsService {
    fun getStationsWithinBounds(bounds: Bounds): List<FuelStation>
}
