package com.bruhascended.trades.util

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.bruhascended.trades.R
import java.util.*

fun Context.getFullDateTime (time: Long): String {
    val smsTime = Calendar.getInstance().apply {
        timeInMillis = time
    }

    val now = Calendar.getInstance()

    val timeString = DateFormat.format(
            if (DateFormat.is24HourFormat(this)) "H:mm" else "h:mm aa",
            smsTime
    ).toString()

    val yesterdayStr = getString(R.string.yesterday)
    return when {
        DateUtils.isToday(time) -> timeString
        DateUtils.isToday(time + DateUtils.DAY_IN_MILLIS) -> "$timeString, $yesterdayStr"
        now[Calendar.YEAR] == smsTime[Calendar.YEAR] ->
            timeString + ", " + DateFormat.format("d MMMM", smsTime).toString()
        else ->
            timeString + ", " + DateFormat.format("dd/MM/yyyy", smsTime).toString()
    }
}