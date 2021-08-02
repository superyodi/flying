package com.yodi.flying.model.repository


import androidx.annotation.WorkerThread
import com.yodi.flying.model.dao.TagDao
import com.yodi.flying.model.entity.Tag
import kotlinx.coroutines.flow.Flow


class TagRepository(private val tagDao: TagDao) {

    fun getTags(userId: Long): Flow<List<Tag>> = tagDao.getTags(userId)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(tag: Tag) {
        tagDao.insert(tag)
    }

}