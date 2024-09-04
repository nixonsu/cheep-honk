package com.nixonsu.cheephonk.clients

import com.nixonsu.cheephonk.domain.Location
import com.nixonsu.cheephonk.domain.TravelInfo

interface TravelInfoService {
    fun getTravelInfoBetween(origin: Location, destination: Location): TravelInfo
}
