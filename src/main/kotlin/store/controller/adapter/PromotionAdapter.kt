package store.controller.adapter

import store.model.Promotion
import store.util.DateTimeUtil.changeStartTime
import store.util.DateTimeUtil.changeEndTime
import java.io.File

class PromotionAdapter {
    fun loadPromotion(): List<Promotion> {
        return File("src/main/resources/promotions.md").readLines().filterNot { it.startsWith("name") }
            .map { parsePromotion(it) }
    }

    private fun parsePromotion(input: String): Promotion {
        val (name, buy, get, startDate, endDate) = input.split(",")
        val startDateTime = changeStartTime(startDate)
        val endDateTime = changeEndTime(endDate)
        return Promotion(name, buy.toInt(), get.toInt(), startDateTime, endDateTime)
    }
}