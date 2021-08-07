package com.yodi.flying.model.repository


import androidx.annotation.WorkerThread
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.TagDao
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.flow.Flow


class TagRepository(private val tagDao: TagDao,
                    private val preferences: SharedPreferenceManager) {

    fun getTags(): Flow<List<Tag>> = tagDao.getTags(preferences.getLong(Constants.PREF_USER_ID))


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(tag: Tag) {
        tagDao.insert(tag)
    }

}