package store.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    fun changeStartTime(input: String): LocalDateTime {
        val dateTimeString = "%s 00:00:00".format(input)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    fun changeEndTime(input: String): LocalDateTime {
        val dateTimeString = "%s 23:59:59".format(input)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.parse(dateTimeString, formatter)
    }
}