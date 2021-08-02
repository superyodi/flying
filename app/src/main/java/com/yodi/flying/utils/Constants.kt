package com.yodi.flying.utils

/**
 * Constants used throughout the app.
 */

class Constants {
    companion object {

        const val DATABASE_NAME = "pomodoro-db"

        // SharedPreference
        const val PREF_FILE_NAME = "com.yodi.flying.sharedprefs"
        const val PREF_USER_ID = "PREF_USER_ID"

        // Intent Actions for communication with timer service
        const val ACTION_START              = "ACTION_START"
        const val ACTION_RESUME             = "ACTION_RESUME"
        const val ACTION_PAUSE              = "ACTION_PAUSE"
        const val ACTION_CANCEL             = "ACTION_CANCEL"
        const val ACTION_CANCEL_AND_RESET   = "ACTION_CANCEL_AND_RESET"
        const val ACTION_INITIALIZE_DATA    = "ACTION_INITIALIZE_DATA"
        const val ACTION_MUTE               = "ACTION_MUTE"
        const val ACTION_VIBRATE            = "ACTION_VIBRATE"
        const val ACTION_SOUND              = "ACTION_SOUND"
        const val ACTION_SHOW_MAIN_ACTIVITY = "ACTION_SHOW_MAIN_ACTIVITY"

        const val EXTRA_POMODORO_ID          = "EXTRA_TIMER_ID"

        const val NOTIFICATION_CHANNEL_ID   = "timer_channel"
        const val NOTIFICATION_CHANNEL_NAME = "타이머"

        const val NOTIFICATION_ID           = 1
        const val NOTIFICATION_COMPLETE_ID  = 2

        const val ONE_SECOND                = 1000L
        const val TIMER_UPDATE_INTERVAL     = 5L    //5ms
        const val TIMER_STARTING_IN_TIME    = 100L // 100ms

        const val KAKAO_SDK_APP_KEY = "677173c40d91ddb9e19c265d231c1b04"


        // 임시 사용자 설정 변수
//const val SHORT_BREAK_TIME = 300000L //5min
        val LONG_BREAK_TIME = 900000L //15min
        //const val RUNNING_TIME = 1500000L //25min
        val RUNNING_TIME = 10000L //10sec // test
        val SHORT_BREAK_TIME = 5000L //5sec // test
        val LONG_BREAK_COUNT = 4


        // User Id
        var USER_ID = 0L

    }


}