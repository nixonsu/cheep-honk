package clients.google

import domain.TravelInfo

data class DistanceMatrixResponse(
    val rows: List<Row>
)

data class Row(
    val elements: List<Element>
)

data class Element(
    val distance: Value,
    val duration: Value
)

data class Value(
    val text: String,
    val value: Int
)

fun DistanceMatrixResponse.toTravelInfo() = TravelInfo(
        distanceInKms = rows.first().elements.first().distance.value,
        durationInSeconds = rows.first().elements.first().duration.value
    )
