package com.yodi.flying.model.repository


import androidx.annotation.WorkerThread
import com.yodi.flying.model.dao.TagDao
import com.yodi.flying.model.entity.Tag
import kotlinx.coroutines.flow.Flow


class TagRepository(private val tagDao: TagDao) {



    val allTags : Flow<List<Tag>> = tagDao.getTags()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(tag: Tag) {
        tagDao.insert(tag)
    }

}