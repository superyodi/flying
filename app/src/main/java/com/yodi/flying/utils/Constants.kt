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




        // 임시 사용자 설정 변수
//const val SHORT_BREAK_TIME = 300000L //5min
        val LONG_BREAK_TIME = 900000L //15min

        //const val RUNNING_TIME = 1500000L //25min
        val RUNNING_TIME = 10000L //10sec // test
        val SHORT_BREAK_TIME = 5000L //5sec // test
        val LONG_BREAK_COUNT = 4



    }



}