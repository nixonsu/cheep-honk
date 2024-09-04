package com.nixonsu.cheephonk.clients

import com.nixonsu.cheephonk.domain.Bounds
import com.nixonsu.cheephonk.domain.FuelStation

interface FuelStationsService {
    fun getStationsWithinBounds(bounds: Bounds): List<FuelStation>
}
