package com.yodi.flying.utils

import com.yodi.flying.model.PomodoroState
import java.util.concurrent.TimeUnit

fun getCityFromTotalTime(
    totalTime: Long,
): String {
    return when (TimeUnit.MILLISECONDS.toHours(totalTime).toInt()) {
        in 0 until 2 -> Constants.TOKYO
        in 2 until 4 -> Constants.HANOI
        in 4 until 6 -> Constants.HAWAII
        in 6 until 8 -> Constants.NEWYORK
        in 8 until 10 -> Constants.HAVANA
        else -> Constants.MOON

    }
}
