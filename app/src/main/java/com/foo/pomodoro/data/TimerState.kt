package com.foo.pomodoro.data

enum class TimerState(val stateName: String) {
    RUNNING("RUNNING"),
    PAUSED("PAUSED"),
    EXPIRED("EXPIRED");
}