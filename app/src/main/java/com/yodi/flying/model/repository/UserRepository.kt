package com.yodi.flying.model.repository

import androidx.annotation.WorkerThread
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.UserDao
import com.yodi.flying.model.entity.User
import com.yodi.flying.service.TimerService
import com.yodi.flying.utils.Constants.Companion.PREF_IS_AUTO_BREAK_MODE
import com.yodi.flying.utils.Constants.Companion.PREF_IS_AUTO_SKIP_MODE
import com.yodi.flying.utils.Constants.Companion.PREF_IS_NON_BREAK_MODE
import com.yodi.flying.utils.Constants.Companion.PREF_LONG_REST_TERM
import com.yodi.flying.utils.Constants.Companion.PREF_LONG_REST_TIME
import com.yodi.flying.utils.Constants.Companion.PREF_RUNNING_TIME
import com.yodi.flying.utils.Constants.Companion.PREF_SHORT_REST_TIME
import com.yodi.flying.utils.Constants.Companion.PREF_USER_ID
import com.yodi.flying.utils.getFormattedStopWatchTime
import timber.log.Timber


class UserRepository(private val userDao : UserDao, private val preferences:
SharedPreferenceManager) {


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUser(userId: Long): User = userDao.getUser(userId)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(user: User) {
        userDao.updateUser(user)
    }

    fun getUserIdFromPreferences() = preferences.getLong(PREF_USER_ID)

    fun setUserIdToPreferences(id : Long) = preferences.setLong(PREF_USER_ID, id)

    fun setUserPreference(user : User) {

        user.let{
            preferences.setLong(PREF_RUNNING_TIME, it.runningTime)
            preferences.setLong(PREF_SHORT_REST_TIME, it.shortBreakTime)
            preferences.setLong(PREF_LONG_REST_TIME, it.longBreakTime)
            preferences.setInt(PREF_LONG_REST_TERM, it.longBreakTerm)
            preferences.setBoolean(PREF_IS_AUTO_BREAK_MODE, it.isAutoBreakMode)
            preferences.setBoolean(PREF_IS_AUTO_SKIP_MODE, it.isAutoSkipMode)
            preferences.setBoolean(PREF_IS_NON_BREAK_MODE, it.isNonBreakMode)
        }
    }

}