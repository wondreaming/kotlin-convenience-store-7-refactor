package store.controller.adapter

import store.model.Promotion
import store.util.Date
import java.io.File

class PromotionAdapter {

    fun loadPromotionType(): List<Promotion> {
        return File("src/main/resources/promotions.md").readLines().filterNot { it.startsWith("name") }.map {
            parsePromotion(it)
        }
    }

    private fun parsePromotion(line: String): Promotion {
        val (name, buy, get, startDateTime, endDateTime) = line.split(",")
        val date = Date()
        return Promotion(
            name, buy.toInt(), get.toInt(), date.changeStartTime(startDateTime), date.changeEndTime(endDateTime)
        )
    }
}