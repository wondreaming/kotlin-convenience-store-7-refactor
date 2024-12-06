package store.util

import java.text.DecimalFormat

object DecimalUtil {
    fun getDecimalFormat(number: Int): String {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(number)
    }
}