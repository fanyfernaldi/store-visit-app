package com.fanyfernaldi.pitjarustest.misc

import java.text.SimpleDateFormat
import java.util.*

fun Date?.toFormattedDate(outputDateFormat: String): String {
    return try {
        val outputFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
        outputFormat.format(this!!)
    } catch (ex: Exception) {
        ex.printStackTrace()
        ""
    }
}