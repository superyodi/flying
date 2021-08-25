package com.yodi.flying.model

enum class TimerState(val stateName: String) {
    RUNNING("RUNNING"),
    PAUSED("PAUSED"),
    DONE("DONE"),
    EXPIRED("EXPIRED");
}