package com.yodi.flying.utils

import android.telecom.ConnectionService
import com.yodi.flying.model.PomodoroState.Companion.FLYING
import com.yodi.flying.model.PomodoroState.Companion.LONG_BREAK
import com.yodi.flying.model.PomodoroState.Companion.NONE
import com.yodi.flying.model.PomodoroState.Companion.SHORT_BREAK
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max


fun calculateRadiusOffset(strokeSize: Float, dotStrokeSize: Float, markerStrokeSize: Float)
        : Float {
    return max(strokeSize, max(dotStrokeSize, markerStrokeSize))
}

fun getFormattedStopWatchTime(ms: Long?): String{
    ms?.let {
        var milliseconds = ms


        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        // Convert to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        // Build formatted String
        return "${if(minutes < 10) "0" else ""}$minutes : " +
                "${if(seconds < 10) "0" else ""}$seconds"
    }
    return ""
}




fun getFormattedTotalTime(ms: Long?): String{
    ms?.let {
        var milliseconds = ms
        // Convert to hours
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)

        return (if (hours <= 0) "" else "${hours}h ") +
                (if (minutes < 10) "0${minutes}m" else "${minutes}m")
    }
    return "0m"
}




fun convertLongToTime(time: Long?): String {
    time?.let {
        val date = Date(time)
        val format = DateFormat.getDateTimeInstance()
        return format.format(date)
    }
    return "No time found!"
}


// yyyyMMdd, yyyy/MM/dd 등
fun convertDateToString(date: Date, pattern: String): String =
    SimpleDateFormat(pattern).format(date)

fun convertDateToString(date: Date?, pattern: String, locale: Locale): String =
    SimpleDateFormat(pattern, locale).format(date)


// (Long) 20210817 ---> (String) 2021/08/17
fun convertLongToString(dateLong: Long?, pattern: String) : String {
    dateLong?.let {
        val df = SimpleDateFormat("yyyyMMdd")
        val date = df.parse(it.toString())
        return convertDateToString(date, pattern)
    }

    return ""
}

fun convertStringToLong(dateString : String?, pattern: String) : Long? {
    dateString?.let {
        val df = SimpleDateFormat(pattern)
        val date = df.parse(dateString)
        return convertDateToLong(date)
    }
    return null

}

fun convertDateToLong(date: Date): Long {
    date?.let {
        val df = SimpleDateFormat("yyyyMMdd")
        df.format(date)?.let {
            return it.toLong()
        }
    }
    return 0L
}


fun convertDateTimeToLong(date: Date): Long {
    date?.let {
        val df = SimpleDateFormat("yyyyMMddHH")
        df.format(date)?.let {
            return it.toLong()
        }
    }
    return 0L
}
/*
e.g)
if intervalStep == 2
selectedValue = 3 --> 1h 30m
selectedValue = 6 --> 3h 00m
 */
fun convertRulerValueToString(value: Int, intervalStep : Int) = when {
        value == 0 -> "00m"
        value in 0 until intervalStep -> "${60/intervalStep*(value % intervalStep)}m"
        value % intervalStep == 0 -> "${value / intervalStep}h 00m"
        else -> "${value / intervalStep}h ${60/intervalStep*(value % intervalStep)}m"
    }

fun convertRulerValueToLong(value: Int, intervalStep : Int) : Long {
    if (intervalStep == 0)
        return TimeUnit.HOURS.toMillis(value.toLong())
    val hour = value / intervalStep
    val minute = (60 / intervalStep) * (value % intervalStep)
    Timber.i("$hour : $minute")
    return TimeUnit.HOURS.toMillis(hour.toLong()) + TimeUnit.MINUTES.toMillis(minute.toLong())

}




fun getNextPomodoroState(state: Int, nowCount: Int, longBreakTerm : Int) = when(state){
    NONE -> FLYING
    FLYING ->
        // long break 주기가 4일 경우
        if (nowCount > 1 && nowCount % longBreakTerm == 0) LONG_BREAK else SHORT_BREAK
    SHORT_BREAK -> FLYING
    LONG_BREAK -> FLYING
    else -> NONE
}

fun getNextCity(currentCity : String) : String {
    return when (currentCity) {
        Constants.JEJU -> Constants.TOKYO
        Constants.TOKYO -> Constants.HANOI
        Constants.HANOI -> Constants.HAWAII
        Constants.HAWAII -> Constants.NEWYORK
        Constants.NEWYORK -> Constants.HAVANA
        Constants.HAVANA -> Constants.MOON
        Constants.MOON -> Constants.MOON
        else -> Constants.JEJU
    }
}

fun getCityFromTotalTime(
    totalTime: Long,
): String {
    return when (TimeUnit.MILLISECONDS.toHours(totalTime)) {
        in 0 until 2 -> Constants.JEJU
        in 2 until 4 -> Constants.TOKYO
        in 4 until 6 -> Constants.HANOI
        in 6 until 8 -> Constants.HAWAII
        in 8 until 10 -> Constants.NEWYORK
        in 10 until 12 -> Constants.HAVANA
        else -> Constants.MOON

    }
}