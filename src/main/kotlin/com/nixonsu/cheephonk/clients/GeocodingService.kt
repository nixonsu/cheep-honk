package com.nixonsu.cheephonk.clients

import com.nixonsu.cheephonk.domain.Location

interface GeocodingService {
    fun getLocationFor(address: String): Location?
}
