package com.yodi.flying.utils

import com.yodi.flying.model.PomodoroState.Companion.FLYING
import com.yodi.flying.model.PomodoroState.Companion.LONG_BREAK
import com.yodi.flying.model.PomodoroState.Companion.NONE
import com.yodi.flying.model.PomodoroState.Companion.SHORT_BREAK
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

fun getFormattedCompletionTime(ms: Long?): String{
    ms?.let {
        var milliseconds = ms
        // Convert to hours
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        // Convert to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return (if(hours <= 0) "" else if(hours < 10) "0$hours:" else "$hours:") +
                (if(minutes <= 0) "" else if(minutes < 10) "0$minutes:" else "$minutes:" ) +
                "${if(seconds < 10) "0" else ""}$seconds" +
                if(hours > 0) " h" else if(minutes > 0) " min" else "sec"
    }
    return ""
}

fun convertLongToTime(time: Long?): String {
    time?.let {
        val date = Date(time)
        val format = DateFormat.getDateTimeInstance()
        return format.format(date)
    }
    return "No time found!"
}

fun convertDateToString(date: Date?): String =
    SimpleDateFormat("yyyy-MM-dd").format(date)

fun convertDateToLong(date: String?): Long {
    date?.let {
        val df = DateFormat.getDateTimeInstance()
        df.parse(date)?.let {
            return it.time
        }
    }
    return 0L
}
/*
e.g)
selectedValue = 3 --> 1h 30m
selectedValue = 6 --> 3h 00m
 */
fun convertRulerValueToString(value : Int) = when {
        value == 0 -> "00m"
        value == 1 -> "30m"
        value % 2 == 0 -> "${value / 2}h 00m"
        else -> "${value / 2}h 30m"
    }




fun getNextPomodoroState(state: Int, nowCount: Int) = when(state){
    NONE -> FLYING
    FLYING ->
        // long break 주기가 4일 경우
        if (nowCount > 1 && nowCount % 4 == 0) LONG_BREAK else SHORT_BREAK
    SHORT_BREAK -> FLYING
    LONG_BREAK -> FLYING
    else -> NONE
}



