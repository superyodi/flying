package com.foo.pomodoro.utils

import androidx.lifecycle.LiveData
import com.foo.pomodoro.data.PomodoroState
import com.foo.pomodoro.data.PomodoroState.Companion.FLYING
import com.foo.pomodoro.data.PomodoroState.Companion.LONG_BREAK
import com.foo.pomodoro.data.PomodoroState.Companion.NONE
import com.foo.pomodoro.data.PomodoroState.Companion.SHORT_BREAK
import java.text.DateFormat
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

        // Convert to hours
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        // Convert to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        // Build formatted String
        return "${if(hours < 10) "0" else ""}$hours : " +
                "${if(minutes < 10) "0" else ""}$minutes : " +
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

fun convertDateToLong(date: String?): Long {
    date?.let {
        val df = DateFormat.getDateTimeInstance()
        df.parse(date)?.let {
            return it.time
        }
    }
    return 0L
}

fun getNextPomodoroState(state: Int, nowCount: Int) = when(state){
    NONE -> FLYING
    FLYING ->
        if (nowCount % 4 == 0) LONG_BREAK else SHORT_BREAK

    SHORT_BREAK -> FLYING
    LONG_BREAK -> FLYING
    else -> NONE
}



