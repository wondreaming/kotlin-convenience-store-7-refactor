package store.model

import java.time.LocalDateTime

data class Promotion(
    val name: String,
    val buy: Int,
    val get: Int,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
)
