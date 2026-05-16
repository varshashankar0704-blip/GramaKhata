package com.example.gramakhata.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateTime = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    private val dateOnly = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val rupees = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    fun formatDateTime(value: Long) = dateTime.format(Date(value))
    fun formatDate(value: Long) = dateOnly.format(Date(value))
    fun formatRupees(value: Double) = rupees.format(value).replace(".00", "")

    fun dayBounds(day: Long): Pair<Long, Long> {
        val c = Calendar.getInstance().apply { timeInMillis = day }
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val start = c.timeInMillis
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return start to c.timeInMillis
    }
}
