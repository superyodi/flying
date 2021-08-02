package com.yodi.flying.model.repository

import androidx.annotation.WorkerThread
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.UserDao
import com.yodi.flying.model.entity.User
import com.yodi.flying.utils.Constants.Companion.PREF_USER_ID


class UserRepository(private val userDao : UserDao, private val preferences:
SharedPreferenceManager) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUser(userId: Long): User = userDao.getUserId(userId)

    fun getUserIdFromPreferences() = preferences.getLong(PREF_USER_ID)

    fun setUserIdToPreferences(id : Long) = preferences.setLong(PREF_USER_ID, id)

}