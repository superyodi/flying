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
        const val PREF_RUNNING_TIME = "PREF_RUNNING_TIME"
        const val PREF_SHORT_REST_TIME = "PREF_SHORT_REST_TIME"
        const val PREF_LONG_REST_TIME = "PREF_LONG_REST_TIME"
        const val PREF_LONG_REST_TERM = "PREF_LONG_REST_TERM"
        const val PREF_IS_AUTO_BREAK_MODE = "PREF_AUTO_BREAK_MODE"
        const val PREF_IS_AUTO_SKIP_MODE = "PREF_AUTO_SKIP_MODE"
        const val PREF_IS_NON_BREAK_MODE = "PREF_IS_NON_BREAK_MODE"




        // Intent extra
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
        const val EXTRA_POMODORO_ID = "EXTRA_TIMER_ID"
        const val EXTRA_NUMBER_PICKER_ID = "EXTRA_NUMBER_PICKER_ID"
        const val EXTRA_NUMBER_PICKER_VAL = "EXTRA_NUMBER_PICKER_VAL"

        // number picker flag
        const val RUNNING_TIME_FLAG = "RUNNING_TIME_FLAG"
        const val SHORT_REST_TIME_FLAG = "SHORT_REST_TIME_FLAG"
        const val LONG_REST_TIME_FLAG = "LONG_REST_TIME_FLAG"
        const val LONG_REST_TERM_FLAG = "LONG_REST_TERM_FLAG"
        const val GOAL_COUNT_FLAG = "GOAL_COUNT_FLAG"

        // Intent Actions for communication with timer service
        const val ACTION_START = "ACTION_START"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_CANCEL = "ACTION_CANCEL"
        const val ACTION_CANCEL_AND_RESET = "ACTION_CANCEL_AND_RESET"
        const val ACTION_INITIALIZE_DATA = "ACTION_INITIALIZE_DATA"
        const val ACTION_MUTE = "ACTION_MUTE"
        const val ACTION_VIBRATE = "ACTION_VIBRATE"
        const val ACTION_SOUND = "ACTION_SOUND"
        const val ACTION_SHOW_MAIN_ACTIVITY = "ACTION_SHOW_MAIN_ACTIVITY"


        // Dialog TAG
        const val DATE_PICKER = "date picker bottomSheet"
        const val TAG_PICKER = "tag picker bottomSheet"
        const val NUMBER_PICKER = "number picker bottomSheet"

        // City Names and Total times
        const val JEJU = "Jeju"
        const val TOKYO = "Tokyo"
        const val HANOI = "Hanoi"
        const val HAWAII = "Hawaii"
        const val NEWYORK = "New York"
        const val HAVANA ="Havana"
        const val MOON = "MOON"






        const val NOTIFICATION_CHANNEL_ID = "timer_channel"
        const val NOTIFICATION_CHANNEL_NAME = "타이머"

        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_COMPLETE_ID = 2

        const val ONE_SECOND = 1000L
        const val TIMER_UPDATE_INTERVAL = 5L    //5ms
        const val TIMER_STARTING_IN_TIME = 100L // 100ms

        const val KAKAO_SDK_APP_KEY = "677173c40d91ddb9e19c265d231c1b04"

        // set up feature 상태 변수
        const val SETUP_STAGE_1 = "SETUP_STAGE_1"
        const val SETUP_STAGE_2 = "SETUP_STAGE_2"
        const val SETUP_STAGE_3 = "SETUP_STAGE_3"
        const val SETUP_STAGE_DONE = "SETUP_STAGE_DONE"

        const val SETUP_TITLE_1 = "닉네임을 만들어주세요"
        const val SETUP_SUB_TITLE_1 = "2글자 이상으로 부탁해요 :)"
        const val SETUP_SUB_TITLE_2 = "없다면 넘어가도 괜찮아요"
        const val SETUP_SUB_TITLE_3 = "제작자들의 목표 집중시간은 8시간이랍니다"


//        const val LONG_BREAK_TIME = "LONG_BREAK_TIME" //15min
//        const val RUNNING_TIME = "RUNNING_TIME" //10sec // test
//        const val SHORT_BREAK_TIME = "SHORT_BREAK_TIME" //5sec // test
//        const val LONG_BREAK_TERM = "LONG_BREAK_TERM"



    }



}